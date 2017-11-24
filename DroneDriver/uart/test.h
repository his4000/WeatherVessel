#ifndef H_TEST
#define H_TEST
struct dusti2c
{
    int num;
    unsigned long dust_pm25;
    unsigned long dust_pm10;
    float gps_x;
    float gps_y;
};
int dustsensor_send(struct dusti2c *msg);
int recordSensorData(struct dusti2c *data);

#endif
