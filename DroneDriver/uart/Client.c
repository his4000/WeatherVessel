#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#include "dustsensor.h"

#define BUFF_SIZE 1024
#define IPSIZE 20

/* struct of Dust and GPS */
struct Result
{
    int num;
    unsigned long dust_pm25;
    unsigned long dust_pm10;
    float GPS_X;
    float GPS_Y;
};

int main(int argc, char **argv)
{
    int sock;               /* Socket */
    int server_addr_size;           /* Server address Size */
    char clntIP[IPSIZE] = "192.168.235.1";  /* Client address */

    struct sockaddr_in server_addr;     /* Server Address */

    struct Result newResult;        /* Declare Struct */
    struct dusti2c receive;

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

    while(1)
    {
        memset(&receive,0,sizeof(struct dusti2c));
        newResult.num=1;
        dustsensor_send(&receive);
        newResult.dust_pm25 = receive.dust_pm25;
        printf("client_pm2.5: %ld\n",newResult.dust_pm25);
        newResult.dust_pm10 = receive.dust_pm10;
        printf("pm10: %ld\n",newResult.dust_pm10);
        newResult.GPS_X = 127.0713514;
        newResult.GPS_Y = 37.5464594;

        recordSensorData(&receive);
        /* Send structure to the Server */
        int tempint = 0;
        tempint = sendto(sock, (struct Result*)&newResult, (BUFF_SIZE+sizeof(newResult)),
                    0, (struct sockaddr*)&server_addr, sizeof(server_addr));
        if(tempint == -1)
        {
            printf("Sent struct size %d\n", tempint);
            printf("sendto() sent a different number of bytes than expected\n");
        }
        usleep(10000000);
    }
    close(sock);

    return 0;
}
