#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#include "data.h"

#define BUFF_SIZE 1024
#define IPSIZE 20
#define PORT 9293
#define IP_ADDR "192.168.235.1"

#define MAX_GPS_LAT 37.545453
#define MAX_GPS_LNG 127.081590
#define MIN_GPS_LAT 37.538002
#define MIN_GPS_LNG 127.071463

#define DELAY_TIME 10

#define MAX_PM_10 75
#define MIN_PM_10 45
#define MAX_PM_25 33
#define MIN_PM_25 18

#define DUST_RANGE 15
#define MOVE_RANGE 0.002

int generateRandomNumber(){
	int ret;

	ret = rand() % DUST_RANGE;

	return ret;
}

void sendData(float GPS_X, float GPS_Y)
{
	int sock;				/* Socket */
	int server_addr_size;			/* Server address Size */
	char clntIP[IPSIZE] = IP_ADDR;	/* Client address */
	struct sockaddr_in server_addr;		/* Server Address */
	struct Result newResult;		/* Declare Struct */
	int randomVal = generateRandomNumber();

	srand(time(NULL));
	/* Create a UDP socket */
	sock = socket(PF_INET, SOCK_DGRAM, 0);

	if(-1 == sock)
	{
		printf("socket() failed\n");
		exit(1);
	}

	/* Construct the server address structure */
	memset(&server_addr, 0, sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(PORT);
	server_addr.sin_addr.s_addr = inet_addr(clntIP);

	newResult.dust_pm25 = MIN_PM_25 + randomVal;
	newResult.dust_pm10 = MIN_PM_10 + (randomVal*2);
	newResult.GPS_X = GPS_X;
	newResult.GPS_Y = GPS_Y;

	printf("GPS_X : %f\nGPS_Y : %f\n", newResult.GPS_X, newResult.GPS_Y);
	printf("pm10 : %ld\npm2.5 : %ld\n", newResult.dust_pm10, newResult.dust_pm25);
	/* Send structure to the Server */
	int tempint = 0;
	tempint = sendto(sock, (struct Result*)&newResult, (BUFF_SIZE+sizeof(newResult)),
			0, (struct sockaddr*)&server_addr, sizeof(server_addr));
	if(tempint == -1)
	{
		printf("Sent struct size %d\n", tempint);
		printf("sendto() sent a different number of bytes than expected\n");
	}	

	close(sock);
	printf("Complete to send data\n");
}

int isInRange(float gps_x, float gps_y){
	int ret = 1;

	if(gps_x < MIN_GPS_LNG || gps_x > MAX_GPS_LNG)
		ret = 0;
	if(gps_y < MIN_GPS_LAT || gps_y > MAX_GPS_LAT)
		ret = 0;

	return ret;
}

void moving(float* gps_x, float* gps_y){
	int dir = rand()%4;
	float current_gps_x = *gps_x;
	float current_gps_y = *gps_y;

	switch(dir){
		case 0 : //Move East
			current_gps_x -= MOVE_RANGE;
			break;
		case 1 : //Move West
			current_gps_x += MOVE_RANGE;
			break;
		case 2 : //Move South
			current_gps_y += MOVE_RANGE;
			break;
		case 3 : //Move North
			current_gps_y -= MOVE_RANGE;
			break;
		default : 
			break;
	}

	if(isInRange(current_gps_x, current_gps_y)){
		(*gps_x) = current_gps_x;
		(*gps_y) = current_gps_y;
	}
}

int main(void){
	float current_gps_x = MAX_GPS_LNG;
	float current_gps_y = MAX_GPS_LAT;

	printf("Port number : %d\n", PORT);

	while(1){
		moving(&current_gps_x, &current_gps_y);
		sendData(current_gps_x, current_gps_y);

		sleep(DELAY_TIME);
	}

	return 0;
}
