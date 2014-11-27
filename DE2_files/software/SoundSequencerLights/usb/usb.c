#include "usb.h"

#include "usb_common.h"
#include "usb_config.h"
#include "usb_device_hal.h"
#include "usb_isr.h"
#include "usb_queue.h"

#include "altera_up_avalon_usb.h"

#include <sys/alt_irq.h>
#include <unistd.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>

//  Global Variable
D13FLAGS bD13flags;
USBCHECK_DEVICE_STATES bUSBCheck_Device_State;
CONTROL_XFER ControlData;
IO_REQUEST idata ioRequest;

unsigned int ISP1362_BASE;
unsigned int usb_initialized = 0;

usb_fifo usb_recv_queue;
usb_fifo usb_send_queue;

void (*usb_recv_cb)(void*) = NULL;
void * usb_recv_cb_context;

void (*usb_send_cb)(void*) = NULL;
void * usb_send_cb_context;

const int packet_size = 64;

void usb_register_recv_callback(void (*fcn)(void*), void * context) {
	usb_recv_cb = fcn;
	usb_recv_cb_context = context;
}

void usb_register_send_callback(void (*fcn)(void*), void * context) {
	usb_send_cb = fcn;
	usb_send_cb_context = context;
}

void usb_recv_queue_push(unsigned char * data, unsigned int len) {
	unsigned int c;
	c = alt_irq_disable_all();
	usb_fifo_push(&usb_recv_queue, data, len);
	alt_irq_enable_all(c);
	if (usb_recv_cb)
		usb_recv_cb(usb_recv_cb_context);
}

void usb_send_queue_pop(unsigned char ** data, unsigned int * len) {
	unsigned int c;
	unsigned int offset;

	c = alt_irq_disable_all();
	usb_fifo_pop(&usb_send_queue, data, len, &offset);
	alt_irq_enable_all(c);

	assert(! offset);
}

void usb_recv_queue_pop(unsigned char ** data, unsigned int * len) {
	unsigned int c;
	unsigned int offset;

	c = alt_irq_disable_all();
	usb_fifo_pop(&usb_recv_queue, data, len, &offset);
	alt_irq_enable_all(c);

	assert(! offset);
}

void usb_device_send(unsigned char * buf, unsigned int len) {
	unsigned int c;
	c = alt_irq_disable_all();

	unsigned int remaining = len;
	while (remaining) {
		// This packet size
		int size = packet_size;
		if (remaining < size)
			size = remaining;

		unsigned char * new_buf = calloc(sizeof(unsigned char), size);
		assert(new_buf);
		memcpy(new_buf, buf + (len - remaining), size);
		usb_send_queue_push(new_buf, size);

		if (remaining == packet_size)
			usb_send_queue_push(NULL, 0);

		remaining -= size;
	}
	alt_irq_enable_all(c);
}

int usb_device_recv(unsigned char * buf, unsigned int len) {
	unsigned int remaining = len;

	unsigned int c;
	c = alt_irq_disable_all();
	while (remaining) {
		// Get pointer to next packet
		usb_packet * packet = usb_fifo_peek(&usb_recv_queue);

		if (!packet)
			break;

		unsigned int left_in_packet = packet->len - packet->offset;

		if (left_in_packet <= remaining) {
			// Pop packet
			unsigned char * packet_data;
			unsigned int packet_len;
			unsigned int packet_offset;
			usb_fifo_pop(&usb_recv_queue, &packet_data, &packet_len,
					&packet_offset);
			memcpy(buf + (len - remaining), packet_data + packet_offset,
					packet_len - packet_offset);
			free(packet_data);
			remaining -= packet_len - packet_offset;
		} else {
			// Take partial packet
			memcpy(buf + (len - remaining), packet->data + packet->offset,
					remaining);
			packet->offset += remaining;
			remaining = 0;
		}
	}

	alt_irq_enable_all(c);
	return len - remaining;
}

void usb_send_queue_push(unsigned char * data, unsigned int len) {
	unsigned int c;
	c = alt_irq_disable_all();
	usb_fifo_push(&usb_send_queue, data, len);
	alt_irq_enable_all(c);
	if (usb_send_cb)
		usb_send_cb(usb_send_cb_context);
}

int usb_send_queue_is_empty() {
	return usb_fifo_is_empty(&usb_send_queue);
}
int usb_recv_queue_is_empty() {
	return usb_fifo_is_empty(&usb_recv_queue);
}

void usb_device_init(alt_up_usb_dev * usb_dev, alt_u32 usb_irq_id) {
	ISP1362_BASE = usb_dev->base;

	usb_fifo_init(&usb_recv_queue);
	usb_fifo_init(&usb_send_queue);

	usb_disable_all_interrupts();
	disconnect_USB();
	usleep(1000000);
	Hal4D13_ResetDevice();
	bUSBCheck_Device_State.State_bits.DEVICE_DEFAULT_STATE = 1;
	bUSBCheck_Device_State.State_bits.DEVICE_ADDRESS_STATE = 0;
	bUSBCheck_Device_State.State_bits.DEVICE_CONFIGURATION_STATE = 0;
	bUSBCheck_Device_State.State_bits.RESET_BITS = 0;
	usleep(1000000);
	reconnect_USB();
	CHECK_CHIP_ID();
	Hal4D13_AcquireD13(usb_irq_id, (void*) usb_isr);
	usb_reenable_all_interrupts();
	bD13flags.bits.verbose = 1;
	usb_register_send_callback(usb_check_send, NULL);
	usb_initialized = 1;
}

void usb_device_poll() {
	if (!usb_initialized) {
		printf("You called usb_device_poll before calling usb_device_init().  Exiting program.\n");
		exit(-1);
	}
	if (bUSBCheck_Device_State.State_bits.RESET_BITS == 1) {
		usb_disable_all_interrupts();
		return;
	}
	if (bD13flags.bits.suspend) {
		usb_disable_all_interrupts();
		bD13flags.bits.suspend = 0;
		usb_reenable_all_interrupts();
		suspend_change();
	} // Suspend Change Handler
	if (bD13flags.bits.DCP_state == USBFSM4DCP_SETUPPROC) {
		usb_disable_all_interrupts();
		SetupToken_Handler();
		usb_reenable_all_interrupts();
	} // Setup Token Handler
	if ((bD13flags.bits.DCP_state == USBFSM4DCP_REQUESTPROC)
			&& !ControlData.Abort) {
		usb_disable_all_interrupts();
		bD13flags.bits.DCP_state = 0x00;
		DeviceRequest_Handler();
		usb_reenable_all_interrupts();
	} // Device Request Handler
}
