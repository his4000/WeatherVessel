#include <stdio.h>
#include <wiringPi.h>
#include <wiringSerial.h>
#include <errno.h>
#include <string.h>

int main()
{
	unsigned char open[6] = {0x11, 0x03, 0x0C, 0x02, 0x1E, 0xC0};
	char send[5] = {0x11, 0x02, 0x0b, 0x01, 0xE1};
	char receive[20];
	char close[6]={0x11,0x03,0x0C,0x01,0x1E,0xC1};
	
	int open_cnt=0;
	int receive_cnt=0;
	int close_cnt=0;		
	
	long pm25,pm10;
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
	
	rtn=write(fd,open,6);
	usleep(5000);
	
	while( (i = serialDataAvail(fd)) >= 0 && open_cnt !=5)
	{
		if (i > 0) {
			printf("%x ",serialGetchar(fd));
			fflush(stdout);
			open_cnt++;
		}
		if (i < 0) {
		perror("serialDataAvail");
	}

		usleep(10);
	}
	while(1)
	{
		rtn=write(fd,send,5);
		usleep(5000);
	
		while( (i = serialDataAvail(fd)) >= 0)
		{
			if (i > 0) {
				receive[receive_cnt++] = serialGetchar(fd);
				if(receive_cnt == 20){
					break;
				}
				
			}
			if (i < 0) 
			{
				perror("serialDataAvail");
			}
			usleep(10);
		}
		pm25 = (long)receive[3]<<24 | (long)receive[4] << 16 | (long)receive[5] << 8 | (long)receive[6];
		pm10 = (long)receive[7]<<24 | (long)receive[8] << 16 | (long)receive[9] << 8 | (long)receive[10];
		printf("\npm2.5: %ld ug/m^3\n",pm25);
		printf("pm10: %ld ug/m^3\n",pm10);
		
		receive_cnt = 0;
		//cnt++;
		usleep(1000000);
		break;
	}
	
	rtn=write(fd,close,6);
	usleep(5000);
	printf("%d\n",rtn);
	while( (i = serialDataAvail(fd)) >= 0 && close_cnt !=5)
	{
		if (i > 0) {
			printf("%x ",serialGetchar(fd));
			fflush(stdout);
			close_cnt++;
		}
		if (i < 0) {
		perror("serialDataAvail");
	}

		usleep(10);
	}
	rtn_dust(pm25,pm10);
	serialClose(fd);
	
}
