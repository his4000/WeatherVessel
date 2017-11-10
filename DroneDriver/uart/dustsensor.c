      1 #include <stdio.h>
      2 #include <stdlib.h>
      3 #include <wiringPi.h>
      4 #include <wiringSerial.h>
      5 #include <errno.h>
      6 #include <string.h>
      7 #include "dustsensor.h"
      8
      9 char receive[20];
     10
     11 unsigned char Checksum_cal(void)
     12 {
     13     unsigned char count, sum =0;
     14
     15     for(count=0;count<19;count++){
     16         sum +=receive[count];
     17     }
     18     return 256-sum;
     19 }
     20 int dustsensor_send(struct dusti2c *msg)
     21 {
     22     //unsigned char open[6] = {0x11, 0x03, 0x0C, 0x02, 0x1E, 0xC0};
     23     char send[5] = {0x11, 0x02, 0x0b, 0x01, 0xE1};
     24     //char close[6]={0x11,0x03,0x0C,0x01,0x1E,0xC1};
     25
     26     int open_cnt=0;
     27     int receive_cnt=0;
     28     int close_cnt=0;
     29
     30     unsigned long pm25,pm10;
     31     int fd;
     32     int rtn;
     33     int i=0;
     34     int cnt=0;
     35
     36
     37     if((fd = serialOpen("/dev/ttyAMA0", 9600))<0)
     38     {
     39         fprintf(stderr,"Unable to open serial device: %s\n", strerror(errno));
     40         return 1;
     41     }
     42     printf("fd: %d\n",fd);
     43     if(wiringPiSetup() == -1)
     44     {
     45         fprintf(stdout,"Unable to start wiringPi: %s\n",strerror(errno));
     46         return 1;
     47     }
     48     printf("wiringPisetup done\n");
     49     /*
     50     rtn=write(fd,open,6);
     51     usleep(5000);
     52
     53     while( (i = serialDataAvail(fd)) >= 0 && open_cnt !=5)
     54     {
     55         if (i > 0) {
     56             printf("%x ",serialGetchar(fd));
     57             fflush(stdout);
     58             open_cnt++;
     59         }
     60         if (i < 0) {
     61         perror("serialDataAvail");
     62
     63         }
     64         usleep(10);
     65     }
     66     */
     67
     68         rtn=write(fd,send,5);
     69         usleep(10000);
     70         printf("rtn: %d\n",rtn);
     71
     72         while( (i = serialDataAvail(fd)) >= 0)
     73         {
     74             if (i > 0) {
     75                 receive[receive_cnt] = serialGetchar(fd);
     76                 receive_cnt++;
     77                 if(receive_cnt == 20){
     78                     break;
     79                 }
     80                 usleep(10);
     81             }
     82             cnt++;
     83             if(cnt>1000){
     84                 break;
     85             }
     86             usleep(10);
     87         }
     88         if(Checksum_cal() != receive[19]){
     89             pm25=-1;
     90             pm10=-1;
     91             memset(receive,0,sizeof(receive));
     92         }
     93         else{
     94             pm25 = (unsigned long)receive[3]<<24 | (unsigned long)receive[4] << 16 | (unsigned long)receive[5] << 8 | (unsigned long)receive[6];
     95             pm10 = (unsigned long)receive[7]<<24 | (unsigned long)receive[8] << 16 | (unsigned long)receive[9] << 8 | (unsigned long)receive[10];
     96             printf("\npm2.5: %ld ug/m^3\n",pm25);
     97             printf("pm10: %ld ug/m^3\n",pm10);
     98         }
     99
    100         (msg->dust_pm25) = pm25;
    101         (msg->dust_pm10) = pm10;
    102
    103     /*
    104     rtn=write(fd,close,6);
    105     usleep(5000);
    106
    107     while( (i = serialDataAvail(fd)) >= 0 && close_cnt !=5)
    108     {
    109         if (i > 0) {
    110             printf("%x ",serialGetchar(fd));
    111             fflush(stdout);
    112             close_cnt++;
    113         }
    114         if (i < 0) {
    115         perror("serialDataAvail");
    116     }
    117
    118         usleep(10);
    119     }
    120     */
    121     usleep(5000);
    122
    123     serialClose(fd);
    124
    125     return 0;
    126 }

