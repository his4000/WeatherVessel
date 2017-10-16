#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#define BUFF_SIZE 1024
#define IPSIZE 20

/* struct of Dust and GPS */
struct Result
{
	long dust_pm25;
	long dust_pm10;
	float GPS_X;
	float GPS_Y;
};

int main(void)
{
	int sock;				/* Socket */
	int server_addr_size;			/* Server address Size */
	char clntIP[IPSIZE] = "192.168.235.1";	/* Client address */

	struct sockaddr_in server_addr;		/* Server Address */

	struct Result newResult;		/* Declare Struct */
	//struct msg_dust receive;

	char conti;

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
	server_addr.sin_port = htons(9293);
	server_addr.sin_addr.s_addr = inet_addr(clntIP);

	/* run forever */
	do
	{	
		/* Value of Dust, GPS_X, GPS_Y */	
		//printf("dust : ");
		//scanf("%f", &newResult.dust);
		//printf("GPS_X : ");
		//scanf("%f", &newResult.GPS_X);
		//printf("GPS_Y : ");
		//scanf("%f", &newResult.GPS_Y);

		/*receive = sendData();
		newResult.dust_pm25 = receive.dust_pm25;
		newResult.dust_pm10 = receive.dust_pm10;
		*/
		newResult.dust_pm25 = 25;
		newResult.dust_pm10 = 50;
		newResult.GPS_X = 127.0713514;
		newResult.GPS_Y = 37.5464594;

		printf("GPS_X : %f\nGPS_Y : %f\n", newResult.GPS_X, newResult.GPS_Y);
		/* Send structure to the Server */
		int tempint = 0;
		tempint = sendto(sock, (struct Result*)&newResult, (BUFF_SIZE+sizeof(newResult)),
				0, (struct sockaddr*)&server_addr, sizeof(server_addr));
		if(tempint == -1)
		{
			printf("Sent struct size %d\n", tempint);
			printf("sendto() sent a different number of bytes than expected\n");
		}	
		conti = 'o';
		printf("continue ? y/n : ");
		conti = getchar();
		getchar();
	}while(conti=='y');

	close(sock);

	return 0;
}
