#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<unistd.h>

#define STR_SIZE 32
#define DRONE_NUM 36
#define DEFAULT_PORT 9000

typedef struct drone{
	int drone_id;
	char min_lat[STR_SIZE];
	char min_lng[STR_SIZE];
	char max_lat[STR_SIZE];
	char max_lng[STR_SIZE];
}DRONE;

void makePortNumber(char* buf, int drone_id){
	int integerPort;

	integerPort = DEFAULT_PORT + drone_id;

	sprintf(buf, "%d", integerPort);
}

void execDroneUnit(DRONE* drone){
	char drone_id[STR_SIZE];
	char cmd[STR_SIZE] = "./Simulator ";
	//char** argv = {"", drone->min_lat, drone->min_lng, drone->max_lat, drone->max_lng};
//	makePortNumber(port, drone->drone_id);
	//strncpy(&argv[0], port, STR_SIZE);
	sprintf(drone_id, "%d", drone->drone_id);
	strcat(cmd, drone_id);
	strcat(cmd, " ");
	strcat(cmd, drone->min_lat);
	strcat(cmd, " ");
	strcat(cmd, drone->min_lng);
	strcat(cmd, " ");
	strcat(cmd, drone->max_lat);
	strcat(cmd, " ");
	strcat(cmd, drone->max_lng);
	strcat(cmd, " ");
	strcat(cmd, "&");

	system(cmd);

	//execl("./", cmd, port, drone->min_lat, drone->min_lng, drone->max_lat, drone->max_lng, 0);
}

int main(void){
	DRONE droneArr[DRONE_NUM] = {
		{2, "37.58", "126.840329", "37.60936", "126.9"},
		{3, "37.58", "126.9", "37.60936", "126.95"},
		{4, "37.58", "126.95", "37.60936", "127"},
		{5, "37.58", "127", "37.60936", "127.05"},
		{6, "37.58", "127.05", "37.60936", "127.1"},
		{32, "37.58", "127.1", "37.60936", "127.130179"},
		{7, "37.56", "126.840329", "37.58", "126.9"},
		{8, "37.56", "126.9", "37.58", "126.95"},
		{9, "37.56", "126.95", "37.58", "127"},
		{10, "37.56", "127", "37.58", "127.05"},
		{11, "37.56", "127.05", "37.58", "127.1"},
		{33, "37.56", "127.1", "37.58", "127.130179"},
		{12, "37.54", "126.840329", "37.56", "126.9"},
		{13, "37.54", "126.9", "37.56", "126.95"},
		{14, "37.54", "126.95", "37.56", "127"},
		{15, "37.54", "127", "37.56", "127.05"},
		{16, "37.54", "127.05", "37.56", "127.1"},
		{34, "37.54", "127.1", "37.56", "127.130179"},
		{17, "37.52", "126.840329", "37.54", "126.9"},
		{18, "37.52", "126.9", "37.54", "126.95"},
		{19, "37.52", "126.95", "37.54", "127"},
		{20, "37.52", "127", "37.54", "127.05"},
		{21, "37.52", "127.05", "37.54", "127.1"},
		{35, "37.52", "127.1", "37.54", "127.130179"},
		{22, "37.5", "126.840329", "37.52", "126.9"},
		{23, "37.5", "126.9", "37.52", "126.95"},
		{24, "37.5", "126.95", "37.52", "127"},
		{25, "37.5", "127", "37.52", "127.05"},
		{26, "37.5", "127.05", "37.52", "127.1"},
		{36, "37.5", "127.1", "37.52", "127.130179"},
		{27, "37.490889", "126.840329", "37.5", "126.9"},
		{28, "37.490889", "126.9", "37.5", "126.95"},
		{29, "37.490889", "126.95", "37.5", "127"},
		{30, "37.490889", "127", "37.5", "127.05"},
		{31, "37.490889", "127.05", "37.5", "127.1"},
		{37, "37.490889", "127.1", "37.5", "125.130179"}
	};
	int i;
	//int state;

	for(i=0;i<DRONE_NUM;i++){
		//if(fork() == 0){
			execDroneUnit(&droneArr[i]);
			//break;
		//}
		sleep(5);
	}

	//wait(&state);

	return 0;
}
