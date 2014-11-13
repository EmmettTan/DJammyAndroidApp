#include "usb/usb.h"
#include "altera_up_avalon_usb.h"
#include "system.h"
#include "sys/alt_timestamp.h"

#include <assert.h>

#define MSG_LENGTH 100

void usb_initialization();
void receive_message(unsigned char *message);
void send_message(unsigned char *message);
void clean_message(unsigned char *message);

int main() {
	unsigned char message[MSG_LENGTH];

	usb_initialization();

	while(1){
		receive_message(message);
		send_message(message);
		clean_message(message);
	}

	return 0;
}

void usb_initialization(){
	printf("USB Initialization\n");
	alt_up_usb_dev * usb_dev;
	usb_dev = alt_up_usb_open_dev(USB_0_NAME);
	assert(usb_dev);
	usb_device_init(usb_dev, USB_0_IRQ);

	printf("Polling USB device.  Run middleman now!\n");
	alt_timestamp_start();
	int clocks = 0;
	while (clocks < 50000000 * 10) {
		clocks = alt_timestamp();
		usb_device_poll();
	}
	printf("Done polling USB\n");
}

void receive_message(unsigned char *message){
	int i;
	int bytes_expected;
	int bytes_recvd;
	int total_recvd;
	unsigned char data;

	// First byte is the number of characters in our message
	bytes_expected = 1;
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(&data, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}

	int num_to_receive = (int) data;
	printf("About to receive %d characters:\n", num_to_receive);

	bytes_expected = num_to_receive + 1;
	total_recvd = 1;
	message[0] = data;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(message + total_recvd, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
	message[num_to_receive+1] = '\0';

	for (i = 0; i < num_to_receive+1; i++) {
		printf("%c", message[i]);
	}
	printf("\n");
}

void send_message(unsigned char *message){
	printf("Sending the message to the Middleman\n");
	// Start with the number of bytes in our message
	unsigned char message_length = strlen(message);
	//usb_device_send(&message_length, 1);

	// Now send the actual message to the Middleman
	usb_device_send(message, message_length);
	printf("Message Echo Complete\n");
}

void clean_message(unsigned char *message){
	int i;
	for (i=0; i<MSG_LENGTH; i++){
		message[i] = '\0';
	}
	printf("cleaned buffer\n");
}
