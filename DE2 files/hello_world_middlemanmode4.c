#include "usb/usb.h"
#include "altera_up_avalon_usb.h"
#include "system.h"
#include "sys/alt_timestamp.h"

#include <assert.h>

#define MSG_LENGTH 100

void usb_initialization();
struct packet receive_message(unsigned char *message, struct packet packet);
void send_message(unsigned char *message, struct packet packet);
void clean_message(unsigned char *message);
//void check_host(int id);

struct packet{
	char id;
	char msgsize;
};

int main() {
	unsigned char message[MSG_LENGTH];
	struct packet packet;
	usb_initialization();

	while (1) {
		packet = receive_message(message, packet);
		send_message(message, packet);
		clean_message(message);
	}

	return 0;
}

void usb_initialization() {
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

struct packet receive_message(unsigned char *message, struct packet packet) {
	int i;
	int bytes_expected;
	int bytes_recvd;
	int total_recvd;
	unsigned char id;
	unsigned char msgdata;

	// First byte is the number of characters in our message
	bytes_expected = 1;
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(&id, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}

	printf("Client ID: %d\t", id);
	packet.id = id;

	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(&msgdata, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
	int msgsize = (int) msgdata;
	printf("Message size: %d\t", msgsize);
	packet.msgsize = msgsize;

	bytes_expected = msgsize;
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(message + total_recvd, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
	message[msgsize] = '\0';

	printf("Message is:");
	for (i = 0; i < msgsize; i++) {
		printf("%c", message[i]);
	}
	printf("\n");
	return packet;
}

void send_message(unsigned char *message, struct packet packet) {
	int i;
	char clientmsg[100];
	clientmsg[0] = packet.id;
	clientmsg[1] = packet.msgsize;
	/*clientmsg[0] = packet.id;
	clientmsg[1] = packet.msgsize;*/
	memcpy(&clientmsg[2], message, packet.msgsize);
//	strncpy(clientmsg, message);
	//strcat(clientmsg, "\0");

//	int size = (int) packet.msgsize + 2;
	printf("Sending the message to the Middleman\n");
	// Start with the number of bytes in our message
	unsigned int message_length = packet.msgsize + 2;
	//usb_device_send(&message_length, 1);

	// Now send the actual message to the Middleman
	usb_device_send(clientmsg, message_length);
	printf("Message Echo Complete: \n");
	for (i = 0; i < message_length; i++) {
		printf("%c", clientmsg[i]);
	}
	printf("\n");
}

/*void check_host(int id) {
	unsigned char host[] = "host";
	if (id == 1) {
		send_message(host);
	}
}*/

void clean_message(unsigned char *message) {
	int i;
	for (i = 0; i < MSG_LENGTH; i++) {
		message[i] = '\0';
	}
	printf("cleaned buffer\n");
}


