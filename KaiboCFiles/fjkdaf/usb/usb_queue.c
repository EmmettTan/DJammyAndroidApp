#include "usb_queue.h"

#include <stdlib.h>
#include <assert.h>



void usb_fifo_init(usb_fifo * fifo) {
	fifo->head = NULL;
	fifo->tail = NULL;
}

void usb_fifo_push(usb_fifo * fifo, unsigned char * data, unsigned int len) {
	usb_packet * new_packet = malloc(sizeof(usb_packet));
	assert(new_packet);
	new_packet->len = len;
	new_packet->offset = 0;
	new_packet->data = data;

	new_packet->next = NULL;
	if (! fifo->head)
		fifo->head = new_packet;
	if (fifo->tail)
		fifo->tail->next = new_packet;
	fifo->tail = new_packet;
}

int usb_fifo_is_empty(usb_fifo * fifo) {
	if (fifo->head == NULL)
		return 1;
	else
		return 0;
}

void usb_fifo_pop(usb_fifo * fifo, unsigned char ** data, unsigned int * len, unsigned int * offset) {
	if (usb_fifo_is_empty(fifo)) {
		*data = NULL;
		*len = 0;
		*offset = 0;
		return;
	}

	usb_packet * packet_removed = fifo->head;
	fifo->head = fifo->head->next;
	if (! fifo->head)
		fifo->tail = NULL;

	*data = packet_removed->data;
	*len = packet_removed->len;
	*offset = packet_removed->offset;
	free(packet_removed);

}

usb_packet * usb_fifo_peek(usb_fifo * fifo) {
	return fifo->head;
}

