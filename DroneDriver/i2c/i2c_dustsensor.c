      1 #include <stdio.h>
      2 #include <stdlib.h>
      3 #include <pigpio.h>
      4 #include "test.h"
      5
      6 #define DUSTSENSOR_I2C_ADDR 0x28
      7 #define DUSTSENSOR_WRITE_ADDR 0x50
      8 #define DUSTSENSOR_READ_ADDR 0x51
      9
     10
     11 int dustsensor_send(struct dusti2c *msg)
     12 {
     13     int open_rtn;
     14     int write_rtn;
     15     int read_rtn;
     16     char buf[7]={0x16,0x07,0x02,0x00,0x24,0x00,0xBD};
     17     char receive[22];
     18     unsigned long pm25,pm10,mode;
     19
     20
     21 if(gpioInitialise()<0) return 1;
     22     open_rtn = i2cOpen(1,DUSTSENSOR_I2C_ADDR,0);
     23
     24     //  printf("open: %d\n",open_rtn);
     25
     26
     27     write_rtn = i2cWriteI2CBlockData(open_rtn,DUSTSENSOR_WRITE_ADDR,buf,7);
     28
     29 usleep(5000);
     30         //printf("write: %d\n",write_rtn);
     31
     32 read_rtn = i2cReadI2CBlockData(open_rtn,DUSTSENSOR_READ_ADDR,receive,22);
     33
     34         //printf("read: %d\n",read_rtn);
     35
     36 pm25 = (unsigned long)receive[5]<<8 | (unsigned long)receive[6];
     37 pm10 = (unsigned long)receive[7]<<8 | (unsigned long)receive[8];
     38 mode = (unsigned long)receive[9]<<8 | (unsigned long)receive[10];
     39
     40
     41         printf("pm2.5: %ld\n",pm25);
     42         printf("pm10: %ld\n",pm10);
     43
     44         (msg->dust_pm25) = pm25;
     45         (msg->dust_pm10) = pm10;
     46         //printf("mode: %ld\n",mode);
     47
     48 i2cClose(open_rtn);
     49 gpioTerminate();
     50
     51     return 0;
     52
     53 }

