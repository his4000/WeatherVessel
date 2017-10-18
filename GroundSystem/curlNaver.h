#ifndef H_CURL
#define H_CURL

#include<stdlib.h>

#include"Server.h"

typedef struct curlResponseData{
	char* response;
	size_t size;
}CURL_RES_DATA;

void initCurlResponseData(CURL_RES_DATA*);
size_t curlWriteFunction(void*, size_t, size_t, CURL_RES_DATA*);
char* qURLencode(char*);
void getAddress(char*, SENSOR_DATA*);

#endif
