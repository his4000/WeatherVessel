      1 #include<stdio.h>
      2 #include<time.h>
      3
      4 #include"dustsensor.h"
      5
      6 int recordSensorData(struct dusti2c* data){
      7     FILE* fp;
      8     time_t timer;
      9     struct tm* t;
     10     char currentTime[20];
     11     char recordBuf[100];
     12
     13     timer = time(NULL);
     14     t = localtime(&timer);
     15     sprintf(currentTime, "%04d%02d%02d%02d%02d%02d"
     16             , t->tm_year+1900, t->tm_mon+1, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
     17
     18     if((fp = fopen("records.txt", "a")) == NULL){
     19         fprintf(stderr, "record file open error\n");
     20         return -1;
     21     }
     22
     23     sprintf(recordBuf, "%f, %f, %ld, %ld, %s\n"
     24             , data->gps_x, data->gps_y, data->dust_pm10, data->dust_pm25, currentTime);
     25
     26     fputs(recordBuf, fp);
     27
     28     fclose(fp);
     29
     30     return 0;
     31 }
