#include <stdio.h>
#include <stdlib.h>
#include <pigpio.h>
#include "test.h"

#define DUSTSENSOR_I2C_ADDR 0x28
#define DUSTSENSOR_WRITE_ADDR 0x50
#define DUSTSENSOR_READ_ADDR 0x51


int dustsensor_send(struct dusti2c *msg)
{
    int open_rtn;
    int write_rtn;
    int read_rtn;
    char buf[7]={0x16,0x07,0x02,0x00,0x24,0x00,0xBD};
    char receive[22];
    unsigned long pm25,pm10,mode;
    
    if(gpioInitialise()<0) return 1;
    open_rtn = i2cOpen(1,DUSTSENSOR_I2C_ADDR,0);
    
    write_rtn = i2cWriteI2CBlockData(open_rtn,DUSTSENSOR_WRITE_ADDR,buf,7);

    usleep(5000);

    read_rtn = i2cReadI2CBlockData(open_rtn,DUSTSENSOR_READ_ADDR,receive,22);
    
    pm25 = (unsigned long)receive[5]<<8 | (unsigned long)receive[6];
    pm10 = (unsigned long)receive[7]<<8 | (unsigned long)receive[8];
    mode = (unsigned long)receive[9]<<8 | (unsigned long)receive[10];
    
    printf("pm2.5: %ld\n",pm25);
    printf("pm10: %ld\n",pm10);
    
    (msg->dust_pm25) = pm25;
    (msg->dust_pm10) = pm10;
    
    i2cClose(open_rtn);
    gpioTerminate();
    
    return 0;
    
}

