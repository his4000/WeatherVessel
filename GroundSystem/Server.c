#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <time.h>

#include "Server.h"
#include "query.h"
#include "curlNaver.h"

void stackInMySql(SENSOR_DATA*);
extern int recordSensorData(SENSOR_DATA*);

int main(void)
{
	int sock;           /* Socket */
	int client_addr_size;       /* Client address size */
	int recvStructSize;     /* size of received struct */

	struct sockaddr_in server_addr; /* Server address */
	struct sockaddr_in client_addr; /* Client address */
	SENSOR_DATA data;

	//SENSOR_DATA* data = (SENSOR_DATA*)malloc(sizeof(SENSOR_DATA));    /* Declare str

	printf("size of SENSOR_DATA : %d\n", sizeof(SENSOR_DATA));

	/* Create socket */
	sock = socket(PF_INET, SOCK_DGRAM, 0);

	if(-1 == sock)
	{
		printf("socket() failed\n");
		exit(1);
	}

	/* Construct local address structure */
	memset(&server_addr, 0, sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(9293);
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);    /* Any incoming interface */

	/* Bind to the local address */
	if(-1 == bind(sock, (struct sockaddr*)&server_addr, sizeof(server_addr)))
	{
		printf("bind() error\n");
		exit(1);
	}

	/* Run forever */
	for(;;)
	{
		memset(&data, 0, sizeof(SENSOR_DATA));
		/* Set the size of the in-out parameter */
		client_addr_size = sizeof(client_addr);
		/* Reveive struct from a client */
		if((recvStructSize = recvfrom(sock, &data, sizeof(SENSOR_DATA),0,
					(struct sockaddr*)&client_addr, &client_addr_size))<0)
		{
			printf("recvfrom() failed");
			printf("Dust_25 : %ld\n", data.dust_pm25);
			printf("Dust_10 : %ld\n", data.dust_pm10);
			printf("GPS_X : %.5f\n", data.GPS_X);
			printf("GPS_Y : %.5f\n", data.GPS_Y);

			stackInMySql(&data);
		}
	}
}

void stackInMySql(SENSOR_DATA* data){
	time_t timer;
	struct tm* t;
	char addressIndex[20] = "NULL";
	int integerAddressIndex;
	char query[255];
	char currentTime[20];

	timer = time(NULL);

	getAddress(addressIndex, data);

	t = localtime(&timer);

	sprintf(currentTime, "%04d%02d%02d%02d%02d%02d",
			t->tm_year+1900,t->tm_mon+1,t->tm_mday,t->tm_hour,t->tm_min,t->tm_sec);
	recordSensorData(data);

	sprintf(query, "insert into observe(gps_x, gps_y, pm10, pm25, time, address_id, drone_id) values(%f, %f, %ld, %ld, %s, %s, 1)", 
			data->GPS_X, data->GPS_Y, data->dust_pm10, data->dust_pm25, currentTime, addressIndex);

	printf("Query : %s\n", query);

	if(sendQuery(query) == 1)
		fprintf(stderr, "send query error");
}
