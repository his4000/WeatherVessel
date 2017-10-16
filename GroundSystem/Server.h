#ifndef H_SERVER
#define H_SERVER

#define BUFF_SIZE 1024

/* struct of Dust and GPS */
typedef struct Sensor{
	unsigned long dust_pm25;
	unsigned long dust_pm10;
	float GPS_X;
	float GPS_Y;
}SENSOR_DATA;

void recvSensorData(SENSOR_DATA*);

#endif
