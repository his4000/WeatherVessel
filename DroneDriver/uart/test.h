      1 #ifndef H_TEST
      2 #define H_TEST
      3 struct dusti2c
      4 {
      5     int num;
      6     unsigned long dust_pm25;
      7     unsigned long dust_pm10;
      8     float gps_x;
      9     float gps_y;
     10 };
     11 int dustsensor_send(struct dusti2c *msg);
     12 int recordSensorData(struct dusti2c *data);
     13
     14 #endif
