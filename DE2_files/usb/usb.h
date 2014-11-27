#ifndef __USB_H__
#define __USB_H__

#include "usb_queue.h"

#include "altera_up_avalon_usb.h"

#include <alt_types.h>

void usb_device_init(alt_up_usb_dev * usb_dev, alt_u32 usb_irq_id);
void usb_device_poll();


extern usb_fifo usb_recv_queue;
extern usb_fifo usb_send_queue;

extern unsigned int ISP1362_BASE;

int usb_device_recv(unsigned char * buf, unsigned int len);
void usb_device_send(unsigned char * buf, unsigned int len);

void usb_register_recv_callback(void (*fcn)(void*), void * context);
void usb_recv_queue_pop(unsigned char ** data, unsigned int * len);
void usb_send_queue_push(unsigned char * data, unsigned int len);

int usb_send_queue_is_empty();
int usb_recv_queue_is_empty();

// Don't call these functions!!!
void usb_recv_queue_push(unsigned char * data, unsigned int len);
void usb_send_queue_pop(unsigned char ** data, unsigned int * len);
void usb_register_send_callback(void (*fcn)(void*), void * context);

#endif
