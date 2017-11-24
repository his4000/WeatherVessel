#include<stdio.h>
#include<time.h>

#include"dustsensor.h"

int recordSensorData(struct dusti2c* data){
    FILE* fp;
    time_t timer;
    struct tm* t;
    char currentTime[20];
    char recordBuf[100];

    timer = time(NULL);
    t = localtime(&timer);
    sprintf(currentTime, "%04d%02d%02d%02d%02d%02d"
            , t->tm_year+1900, t->tm_mon+1, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);

    if((fp = fopen("records.txt", "a")) == NULL){
        fprintf(stderr, "record file open error\n");
        return -1;
    }

    sprintf(recordBuf, "%f, %f, %ld, %ld, %s\n"
            , data->gps_x, data->gps_y, data->dust_pm10, data->dust_pm25, currentTime);

    fputs(recordBuf, fp);

    fclose(fp);

    return 0;
}
