#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>
#include <wiringSerial.h>
#include <errno.h>
#include <string.h>
#include "dustsensor.h"

char receive[20];

unsigned char Checksum_cal(void)
{
    unsigned char count, sum =0;

    for(count=0;count<19;count++){
        sum +=receive[count];
    }
    return 256-sum;
}
int dustsensor_send(struct dusti2c *msg)
{
    char send[5] = {0x11, 0x02, 0x0b, 0x01, 0xE1};

    int open_cnt=0;
    int receive_cnt=0;
    int close_cnt=0;

    unsigned long pm25,pm10;
    int fd;
    int rtn;
    int i=0;
    int cnt=0;

    if((fd = serialOpen("/dev/ttyAMA0", 9600))<0)
    {
        fprintf(stderr,"Unable to open serial device: %s\n", strerror(errno));
        return 1;
    }
    printf("fd: %d\n",fd);
    if(wiringPiSetup() == -1)
    {
        fprintf(stdout,"Unable to start wiringPi: %s\n",strerror(errno));
        return 1;
    }
    printf("wiringPisetup done\n");

    rtn=write(fd,send,5);
    usleep(10000);
    printf("rtn: %d\n",rtn);
 
    while( (i = serialDataAvail(fd)) >= 0)
    {
        if (i > 0) {
            receive[receive_cnt] = serialGetchar(fd);
            receive_cnt++;
            if(receive_cnt == 20){
                break;
            }
            usleep(10);
        }
        cnt++;
        if(cnt>1000){
            break;
        }
        usleep(10);
    }
    if(Checksum_cal() != receive[19]){
        pm25=-1;
        pm10=-1;
        memset(receive,0,sizeof(receive));
    }
    else{
        pm25 = (unsigned long)receive[3]<<24 | (unsigned long)receive[4] << 16 | (unsigned long)receive[5] << 8 | (unsigned long)receive[6];
        pm10 = (unsigned long)receive[7]<<24 | (unsigned long)receive[8] << 16 | (unsigned long)receive[9] << 8 | (unsigned long)receive[10];
        printf("\npm2.5: %ld ug/m^3\n",pm25);
        printf("pm10: %ld ug/m^3\n",pm10);
    }
    (msg->dust_pm25) = pm25;
    (msg->dust_pm10) = pm10;
   
    usleep(5000);

    serialClose(fd);

    return 0;
}

