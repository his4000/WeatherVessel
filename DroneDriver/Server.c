#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#define BUFF_SIZE 1024

/*struct of Dust and GPS */
struct Result
{
	long dust_pm25;
	long dust_pm10;
	float GPS_X;
	float GPS_Y;
};

int main(void)
{
	int sock;			/* Socket */
	int client_addr_size;		/* Client address size */
	int recvStructSize;		/* size of received struct */

	struct sockaddr_in server_addr;	/* Server address */
	struct sockaddr_in client_addr;	/* Client address */

	struct Result * temp = malloc(sizeof(struct Result));	/* Declare struct and memory allocation */

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
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);	/* Any incoming interface */

	/* Bind to the local address */
	if(-1 == bind(sock, (struct sockaddr*)&server_addr, sizeof(server_addr)))
	{
		printf("bind() 실행 에러\n");
		exit(1);
	}

	/* Run forever */
	for(;;)
	{
		/* Set the size of the in-out parameter */
		client_addr_size = sizeof(client_addr);
		printf("1\n");
		/* Reveive struct from a client */
		if((recvStructSize = recvfrom(sock, temp, sizeof(*temp),0,
					(struct sockaddr*)&client_addr, &client_addr_size))<0)
		{
			printf("recvfrom() failed");
		}
		printf("2\n");
		printf("Dust_25 : %ld\n", temp->dust_pm25);
		printf("Dust_10 : %ld\n", temp->dust_pm10);
		printf("GPS_X : %.5f\n", temp->GPS_X);
		printf("GPS_Y : %.5f\n", temp->GPS_Y);
	}
}
