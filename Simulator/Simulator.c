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
#define IP_ADDR "192.168.0.10"
#define PORT 9293

#define DELAY_TIME 10

#define MAX_PM_10 75
#define MIN_PM_10 45
#define MAX_PM_25 33
#define MIN_PM_25 18

#define DUST_RANGE 15
#define MOVE_RANGE 0.01

float MAX_GPS_LAT;
float MAX_GPS_LNG;
float MIN_GPS_LAT;
float MIN_GPS_LNG;
int DRONE_ID;

int generateRandomNumber(){
	int ret;

	ret = rand() % DUST_RANGE;

	return ret;
}

void sendData(int drone_id, float GPS_X, float GPS_Y)
{
	int sock;				/* Socket */
	int server_addr_size;			/* Server address Size */
	struct sockaddr_in server_addr;		/* Server Address */
	struct Result newResult;		/* Declare Struct */
	int randomVal = generateRandomNumber();

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
	server_addr.sin_addr.s_addr = inet_addr(IP_ADDR);

	newResult.drone_id = drone_id;
	newResult.dust_pm25 = MIN_PM_25 + randomVal;
	newResult.dust_pm10 = MIN_PM_10 + (randomVal*2);
	newResult.GPS_X = GPS_X;
	newResult.GPS_Y = GPS_Y;

//	printf("GPS_X : %f\nGPS_Y : %f\n", newResult.GPS_X, newResult.GPS_Y);
//	printf("pm10 : %ld\npm2.5 : %ld\n", newResult.dust_pm10, newResult.dust_pm25);
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
	float mov_range = (((float)rand())/((float)RAND_MAX))*MOVE_RANGE;
	float current_gps_x = *gps_x;
	float current_gps_y = *gps_y;

	switch(dir){
		case 0 : //Move East
			current_gps_x -= mov_range;
			break;
		case 1 : //Move West
			current_gps_x += mov_range;
			break;
		case 2 : //Move South
			current_gps_y += mov_range;
			break;
		case 3 : //Move North
			current_gps_y -= mov_range;
			break;
		default : 
			break;
	}

	if(isInRange(current_gps_x, current_gps_y)){
		(*gps_x) = current_gps_x;
		(*gps_y) = current_gps_y;
	}
}

int initGlobalValues(char** argv){
	sscanf(argv[1], "%d", &DRONE_ID);
	sscanf(argv[2], "%f", &MIN_GPS_LAT);
	sscanf(argv[3], "%f", &MIN_GPS_LNG);
	sscanf(argv[4], "%f", &MAX_GPS_LAT);
	sscanf(argv[5], "%f", &MAX_GPS_LNG);
}

int main(int argc, char** argv){
	float current_gps_x;
	float current_gps_y;

	if(argc != 6){
		fprintf(stderr, "argument error %d\n", argc);
		return -1;
	}

	srand(time(NULL));

	initGlobalValues(argv);

	current_gps_x = MAX_GPS_LNG;
	current_gps_y = MAX_GPS_LAT;

//	printf("Port number : %d\n", PORT);

	while(1){
		moving(&current_gps_x, &current_gps_y);
		sendData(DRONE_ID, current_gps_x, current_gps_y);

		sleep(DELAY_TIME);
	}

//	printf("Complete to send data\n");

	return 0;
}
