#include "usb/usb.h"
#include "altera_up_avalon_usb.h"
#include "system.h"
#include "sys/alt_timestamp.h"

#include <assert.h>

#define MSG_LENGTH 256
#define MSG_TYPE_BROADCAST_KEYS 1
#define MSG_TYPE_SET_SOUND_OUT 2
#define MSG_TYPE_MUTE 3

void usb_initialization();
struct packet receive_message(unsigned char *message, struct packet packet);
void send_message(unsigned char *message, struct packet packet);
void clean_message(unsigned char *message);
void broadcast_keys(unsigned char *message, struct packet packet, unsigned char receiver_client);
void set_master_device(struct packet packet, unsigned char *receiver_client);

struct packet{
	unsigned char id;
	unsigned char msgsize;
	unsigned char type;
};

int main() {
	unsigned char message[MSG_LENGTH];
	struct packet packet;
	unsigned char receiver_client = 255; //255 means broadcast
	usb_initialization();

	while (1) {
		packet = receive_message(message, packet);
		switch(packet.type){
			case MSG_TYPE_BROADCAST_KEYS:
				broadcast_keys(message, packet, receiver_client); // broadcast keys
				break;
			case MSG_TYPE_SET_SOUND_OUT:
				set_master_device(packet, &receiver_client);
				break;
			default:
				printf("MESSAGE TYPE NOT RECOGNIZED!");
				break;
		}
//		clean_message(message);
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
	unsigned char msg_type;

	// 1st byte is the client id
	bytes_expected = 1;
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(&id, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
//	printf("Client ID: %d\t", id);
	packet.id = id;

	// 2nd byte is the message size
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(&msgdata, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
	int msgsize = (int) msgdata;
//	printf("Message size: %d\t", msgsize);
	packet.msgsize = msgsize;

	// 3rd byte is the message type (sent from the android)
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(&msg_type, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
//	printf("Message type: %d\t", msg_type);
	packet.type = msg_type;

	// Reads the rest of the message
	bytes_expected = msgsize -1; // Considering the msgsize contains the message type
	total_recvd = 0;
	while (total_recvd < bytes_expected) {
		bytes_recvd = usb_device_recv(message + total_recvd, 1);
		if (bytes_recvd > 0)
			total_recvd += bytes_recvd;
	}
	message[msgsize] = '\0';

//	printf("Message is:");
//	for (i = 0; i < msgsize; i++) {
//		printf("%c", message[i]);
//	}
//	printf("\n");
	return packet;
}

void set_master_device(struct packet packet, unsigned char *receiver_client){
	unsigned char master_id = packet.id;
	packet.id = master_id;
	packet.type = MSG_TYPE_SET_SOUND_OUT;
	send_message("", packet);

	packet.id = 255;  // brodcast message
	packet.type = MSG_TYPE_MUTE;
	send_message("", packet);

	*receiver_client = master_id;
}

void broadcast_keys(unsigned char *message, struct packet packet, unsigned char receiver_client){
	message[0] = packet.id; // Puts the client id in the first byte of the message (for the android)
	packet.id = receiver_client; // sets the packet id to the receiver client id. 255 is for broadcast
	send_message(message, packet);
}

void send_message(unsigned char *message, struct packet packet) {
	int i;
	unsigned char clientmsg[MSG_LENGTH];
	clientmsg[0] = packet.id;
	clientmsg[1] = packet.msgsize;
	clientmsg[2] = packet.type;
	memcpy(&clientmsg[3], message, packet.msgsize); // Considering the msgsize contains the message type
	unsigned int message_length = packet.msgsize + 2;

//	printf("Sending the message to the Middleman\n");
	usb_device_send(clientmsg, message_length);
//	printf("Message Echo Complete: \n");
//	for (i = 0; i < message_length; i++) {
//		printf("%c", clientmsg[i]);
//	}
//	printf("\n");
}

void clean_message(unsigned char *message) {
	int i;
	for (i = 0; i < MSG_LENGTH; i++) {
		message[i] = '\0';
	}
//	printf("cleaned buffer\n");
}


