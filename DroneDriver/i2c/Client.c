 #include <stdio.h>
      2 #include <stdlib.h>
      3 #include <string.h>
      4 #include <unistd.h>
      5 #include <arpa/inet.h>
      6 #include <sys/types.h>
      7 #include <sys/socket.h>
      8
      9 #include "test.h"
     10
     11 #define BUFF_SIZE 1024
     12 #define IPSIZE 20
     13
     14 /* struct of Dust and GPS */
     15 struct Result
     16 {
     17     int num;
     18     unsigned long dust_pm25;
     19     unsigned long dust_pm10;
     20     float GPS_X;
     21     float GPS_Y;
     22 };
     23
     24 int main(int argc, char **argv)
     25 {
     26     int sock;               /* Socket */
     27     int server_addr_size;           /* Server address Size */
     28     char clntIP[IPSIZE] = "192.168.1.10";   /* Client address */
     29
     30     struct sockaddr_in server_addr;     /* Server Address */
     31
     32     struct Result newResult;        /* Declare Struct */
     33     struct dusti2c receive;
     34
     35     /* Create a UDP socket */
     36     sock = socket(PF_INET, SOCK_DGRAM, 0);
     37
     38     if(-1 == sock)
     39     {
     40         printf("socket() failed\n");
     41         exit(1);
     42     }
     43
     44     /* Construct the server address structure */
     45     memset(&server_addr, 0, sizeof(server_addr));
     46     server_addr.sin_family = AF_INET;
     47     server_addr.sin_port = htons(9293);
     48     server_addr.sin_addr.s_addr = inet_addr(clntIP);
     49     while(1)
     50     {
     51     memset(&receive,0,sizeof(struct dusti2c));
     52     newResult.num=1;
     53     dustsensor_send(&receive);
     54     newResult.dust_pm25 = receive.dust_pm25;
     55     printf("client_pm2.5: %ld\n",newResult.dust_pm25);
     56     newResult.dust_pm10 = receive.dust_pm10;
     57     printf("pm10: %ld\n",newResult.dust_pm10);
     58     newResult.GPS_X = 127.077559;
     59     newResult.GPS_Y = 37.543608;
     60     recordSensorData(&receive);
     61     /* Send structure to the Server */
     62     int tempint = 0;
     63     tempint = sendto(sock, (struct Result*)&newResult, (BUFF_SIZE+sizeof(newResult)),
     64                 0, (struct sockaddr*)&server_addr, sizeof(server_addr));
     65         if(tempint == -1)
     66         {
     67             printf("Sent struct size %d\n", tempint);
     68             printf("sendto() sent a different number of bytes than expected\n");
     69         }
     70         else{
     71         usleep(5000000);
     72         }
     73         printf("\n");
     74     }
     75     close(sock);
     76
     77     return 0;
     78 }
Client.c

