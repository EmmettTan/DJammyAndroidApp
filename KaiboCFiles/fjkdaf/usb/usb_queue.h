#ifndef __USB_QUEUE_H__
#define __USB_QUEUE_H__

typedef struct usb_fifo usb_fifo;
typedef struct usb_packet usb_packet;

struct usb_fifo {
	struct usb_packet * head;
	struct usb_packet * tail;
};


struct usb_packet {
	int len;
	int offset;
	unsigned char * data;
	struct usb_packet * next;
};

void usb_fifo_init(usb_fifo * fifo);
void usb_fifo_push(usb_fifo * fifo, unsigned char * data, unsigned int len);
int usb_fifo_is_empty(usb_fifo * fifo);
void usb_fifo_pop(usb_fifo * fifo, unsigned char ** data, unsigned int * len, unsigned int * offset);
usb_packet * usb_fifo_peek(usb_fifo * fifo);

#endif
