#include<stdio.h>
#include<string.h>
#include<curl/curl.h>

#include"curlNaver.h"

/*
* This component needs to curl library.
*
* If you don't have curl library,
* you have to download library, first.
*
* If you want to compile this component,
* you should add -L/usr/lib/x86_64-linux-gnu -lcurl.
*
* Simply, you can just add -lcurl.
*/
void initCurlResponseData(CURL_RES_DATA* res){
	res->size = 0;
	res->response = malloc(res->size+1);

	if(res->response == NULL){
		fprintf(stderr, "malloc() failed\n");
		exit(EXIT_FAILURE);
	}

	res->response[0] = '\0';
}

size_t curlWriteFunction(void* ptr, size_t size, size_t nmemb, CURL_RES_DATA* res){
	size_t newLen = res->size + size*nmemb;
	res->response = realloc(res->response, newLen+1);
	if(res->response == NULL){
		fprintf(stderr, "realloc() failed\n");
		exit(EXIT_FAILURE);
	}
	memcpy(res->response+res->size, ptr, size * nmemb);
	res->response[newLen] = '\0';
	res->size = newLen;

	return size * nmemb;
}

char* qURLencode(char* str){
	char* encstr, buf[2+1];
	unsigned char c;
	int i, j;

	if(str == NULL) return NULL;
	fprintf(stderr, "parameter is empty");
	if((encstr = (char*)malloc((strlen(str)*3)+1)) == NULL)
		return NULL;

	for(i=j=0;str[i];i++){
		c = (unsigned char)str[i];
		if((c>='0')&&(c<='9')) encstr[j++] = c;
		else if((c>='A')&&(c<='Z')) encstr[j++] = c;
		else if((c>='a')&&(c<='z')) encstr[j++] = c;
		else if((c=='@')||(c=='.')||(c=='/')||(c=='\\')||(c=='-')||(c=='_')||(c==':'))
		encstr[j++] = c;
		else{
			sprintf(buf, "%02x", c);
			encstr[j++] = '%';
			encstr[j++] = buf[0];
			encstr[j++] = buf[1];
		}
	}
	encstr[j] = '\0';

	return encstr;
}
	
void getAddress(char* buf, SENSOR_DATA* data){
	CURL* curl;
	CURLcode res;
	CURL_RES_DATA resData;
	struct curl_slist* list = NULL;
	char default_url[255]
		= "http://ec2-52-78-23-33.ap-northeast-2.compute.amazonaws.com:8090/getAddressCode/";
	char gps_address[255];
	//char encoded_gps[255];
	char* encoded_gps;

	initCurlResponseData(&resData);

	curl = curl_easy_init();

	printf("success init curl\n");

	if(curl){
		sprintf(gps_address, "%f,%f/", data->GPS_X, data->GPS_Y);
		encoded_gps = qURLencode(gps_address);
		printf("default url : %s\n", default_url);
		printf("encoded gps : %s\n", encoded_gps);
		strcat(default_url, encoded_gps);
		printf("complete parameter : %s\n", default_url);
		curl_easy_setopt(curl, CURLOPT_URL, default_url);

		curl_easy_setopt(curl, CURLOPT_WRITEDATA, &resData);
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, curlWriteFunction);

		printf("ready to curl perform\n");

		if((res = curl_easy_perform(curl)) == 0){
			printf("Everything is OK.\n");
			printf("result of curl : %s\n", resData.response);
			strncpy(buf, resData.response, sizeof(resData.response));
		}
		else{
			printf("curl fail\n");
		}

		free(resData.response);

		curl_easy_cleanup(curl);
	}
}
