#include<stdio.h>
#include<unistd.h>
#include<string.h>

#define DRONE_NUMBER 36
#define STR_SIZE 32

void makeCommand(char* buf, int drone_id){
	char cmd[STR_SIZE] = "./server ";
	char port[STR_SIZE];

	sprintf(port, "%d", 9000+drone_id);
	strcat(cmd, port);
	strcat(cmd, " ");
	strcat(cmd, "&");

	strncpy(buf, cmd, STR_SIZE);
}

int main(void){
	int i;
	char cmd[STR_SIZE];

	for(i=0;i<DRONE_NUMBER;i++){
		makeCommand(cmd, i);
		system(cmd);
	}

	return 0;
}
