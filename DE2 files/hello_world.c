#include "usb/usb.h"
#include "altera_up_avalon_usb.h"
#include "system.h"
#include "sys/alt_timestamp.h"

#include <assert.h>
int init_usb();

int main() {


	int i;
	int bytes_expected;
	int bytes_recvd;
	int total_recvd;
	unsigned char data;
	unsigned char msgdata;
	unsigned char message_tx[] = "EECE381 is so much fun";
	unsigned char message_rx[100];
	int id;
	int msg_length;

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

	printf("Sending the message to the Middleman\n");

	// Start with the number of bytes in our message
	unsigned char message_length = strlen(message_tx);
	usb_device_send(&message_length, 1);

	// Now send the actual message to the Middleman
	usb_device_send(message_tx, message_length);

	// Now receive the message from the Middleman
	printf("Waiting for data to come back from the Middleman\n");
while(1){
	// First byte is the number of characters in our message
	bytes_expected = 1;
	total_recvd = 0;
	data = 0;
	msgdata = 0;

	//finds id
	while (total_recvd < bytes_expected) {

			bytes_recvd = usb_device_recv(&data, 1);

			if (bytes_recvd > 0){
				total_recvd += bytes_recvd;

			}
		}
	id = data;
	printf("Id is: %i \t", id);
	if(id==1){
		unsigned char master[] = "host";

		usb_device_send(master, strlen(master));
	}


	total_recvd = 0;
//finds msg size
	while (total_recvd < bytes_expected) {

		bytes_recvd = usb_device_recv(&msgdata, 1);

		if (bytes_recvd > 0){
			total_recvd += bytes_recvd;

		}
	}
	msg_length = msgdata;
	printf("Message Length is: %i \t", msg_length);

	int num_to_receive = msgdata;
	printf("About to receive %d characters:\n", num_to_receive);

	bytes_expected = num_to_receive;
	//bytes_expected = 2;

	total_recvd = 0;
	while (total_recvd < num_to_receive) {
		bytes_recvd = usb_device_recv(message_rx + total_recvd, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}

	for (i = 0; i < num_to_receive; i++) {
		//printf("%i\t", (int)message_rx[i]);
		printf("%c\t", message_rx[i]);
	}

	printf("\n");
	printf("Message Echo Complete\n");


}
	//return 0;
}

int init_usb() {

	alt_up_usb_dev * usb_dev;
	usb_dev = alt_up_usb_open_dev(USB_0_NAME);
	assert(usb_dev);
	usb_device_init(usb_dev, USB_0_IRQ);
	printf("Polling USB device.\nInstall the USB driver now.\n");
	while (1) {
		usb_device_poll();

	}
}

