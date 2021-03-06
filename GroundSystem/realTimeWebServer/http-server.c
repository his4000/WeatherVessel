#include<stdio.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<string.h>
#include<unistd.h>
#include<sys/time.h>
#include<errno.h>
#include<time.h>
#include<stdlib.h>
#define MAXCLNT 25
#define LENBUFF 256

void errProc(const char*);
int processMsg(char* rBUff, int readLen, int sock);
int transmitReply(int sock);
int sendFile(FILE* fp);
int readLineFromMsg(char* out, int* point, char* in);


char* dayName[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
char* monthName[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
	"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
char filePath[LENBUFF];
char errPath[] = "error.html";

int main(int argc, char** argv)
{
	/* variables */
	int listenSd, connectSd;
	struct sockaddr_in srvAddr, clntAddr;
	int clntAddrLen, readLen, strLen;
	char rBuff[BUFSIZ];
	int maxFd = 0;
	fd_set defaultFds, rFds;
	int res, i;

	/*parameter check*/
	if(argc != 2)
	{
		printf("Usage: %s [Port] \n", argv[0]);
		return -1;
	}
	/* socket */
	listenSd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if(listenSd == -1)	errProc("socket");
	/* address assign */
	memset(&srvAddr, 0, sizeof(srvAddr));
	srvAddr.sin_addr.s_addr = htonl(INADDR_ANY);
	srvAddr.sin_family = AF_INET;
	srvAddr.sin_port = htons(atoi(argv[1]));
	/* bind and listen */
	res = bind(listenSd, (struct sockaddr*)&srvAddr,
			sizeof(srvAddr));
	if(res == -1)	errProc("bind");
	res = listen(listenSd, MAXCLNT);
	if(res == -1)	errProc("listen");
	/* FD setting */
	FD_ZERO(&defaultFds);
	FD_SET(listenSd, &defaultFds);
	maxFd = listenSd;

	while(1)
	{
		rFds = defaultFds;
		res = select(maxFd + 1, &rFds, 0, 0, NULL);
		if(res == -1)	errProc("select");
		for(i=0;i<maxFd+1;i++)
		{
			if(FD_ISSET(i, &rFds))
			{
				if(i == listenSd)
				{
					printf("A client is connected....\n");
					connectSd = accept(listenSd,
							(struct sockaddr*)&clntAddr,
							&clntAddrLen);
					if(connectSd == -1)	continue;
					FD_SET(connectSd, &defaultFds);
					if(maxFd < connectSd)	maxFd = connectSd;
				}
				else
				{
					printf("A message is received...\n");
					readLen = read(i, rBuff, BUFSIZ-1);
					if(readLen == -1)	errProc("read");
					if(readLen == 0)	/* client sends FIN */
					{
						FD_CLR(i, &defaultFds);
						close(i);
						continue;
					}
					processMsg(rBuff, readLen, i);
				}
			}
		}
	}
}

int processMsg(char* rBuff, int readLen, int sock)
{
	int counter = 0;
	int compare = 0;
	int pointer = 4;
	int i;
	char lineBuff[LENBUFF];
	char fileTmp[LENBUFF];
	readLineFromMsg(lineBuff, &counter, rBuff);
	/* Method Get */
	compare = 3;
	if(readLen - counter < compare)	errProc("processMsg");
	if(!strncmp(lineBuff, "GET", compare))
	{
		parseByNullMsg(fileTmp, &pointer, lineBuff);
		fileTmp[pointer-compare-1] = '\0';
		memcpy(filePath, fileTmp+1, pointer-compare-1);

		transmitReply(sock);
	}
	else
	{
//		SendErrMsg();
	}
	readLineFromMsg(lineBuff, &counter, rBuff);
}
int transmitReply(int sock)
{
	time_t t;
	FILE* fp;
	int tempSock;
	char timeVal[30];
	char statusLine[] = "HTTP/1.1 200 OK\r\n";
	char* generalHeader;
	char responseHeader[] = "Server: Freelec/1.0.0\r\nPragma: no-cache\r\ncache-control: private\r\n";
	char entityHeader[] = "Content-Type: text/html; charset=UTF-8\r\n";
	char connectType[] = "Connection: close\r\n";
	struct tm gmtm;
	time(&t);
	gmtime_r(&t, &gmtm);
	strftime(timeVal, 30, "xxx, %d xxx %Y %H:%M:%S GMT", &gmtm);
	memcpy(timeVal, dayName[gmtm.tm_wday], 3);
	memcpy(timeVal+8, monthName[gmtm.tm_mon], 3);
	generalHeader = malloc(36);
	strncpy(generalHeader, "Date: ", 6);
	memcpy(generalHeader+6, timeVal, 30);
	tempSock = dup(sock);
	fp = fdopen(tempSock, "w");
	fputs(statusLine, fp);
	fwrite(generalHeader, 1, 36, fp);
	fputs("\r\n", fp);
	fputs(responseHeader, fp);
	fputs(entityHeader, fp);
	fputs(connectType, fp);
	fputs("\r\n", fp);
	sendFile(fp);
	fflush(fp);
	fclose(fp);
	free(generalHeader);
}

int sendFile(FILE* fp)
{
	FILE* in;
	char buffer[BUFSIZ];
	char c;
	fprintf(stderr, "%s\n", filePath);

	in = fopen(filePath, "r");
	if(in == NULL) errProc("fopen");
	while(1)
	{
		c = fgetc(in);
		if(feof(in))	break;
		fputc(c, fp);
	}
	fclose(in);
}

int readLineFromMsg(char* out, int* point, char* in)
{
	char* start, *ptr;
	int counter;
	ptr = in;
	ptr += *point;
	start = ptr;
	counter = 0;
	while(1)
	{
		if(*ptr != 0x0d)
		{
			ptr++;
			counter++;
		}
		else
		{
			ptr++;
			counter++;
			if(*ptr == 0x0a)	break;
		}
	}
	counter++;
	memcpy(out, start, counter);
	return counter;
}
int parseByNullMsg(char* out, int* point, char* in)
{
	char* start, *ptr;
	int counter;
	ptr = in;
	ptr += *point;
	start = ptr;
	counter = 0;
	while(*ptr != 0x20)
	{
		ptr++;
		counter++;
	}

	memcpy(out, start, counter);
	*point += counter;
	return counter+1;
}

void errProc(const char* str)
{
	fprintf(stderr, "%s: %s \n", str, strerror(errno));
	exit(errno);
}

int sendErrMsg(int sock)
{
	FILE* fp;
	FILE* in;
	int tempSock;
	char c;
	char statusLine[] = "HTTP/1.1 405 Method Not Allowed\r\n";
	char responseHeader[] = "Server: Freelec/1.0.0\r\nPragma: no-cache\r\ncache-control: private\r\n";
	char entityHeader[] = "Content-Type: text/html; charset=UTF-8\r\n";
	char connectType[] = "Connection: close\r\n";
	tempSock = dup(sock);
	fp = fdopen(tempSock, "w");

	fprintf(stderr, "%s\n", filePath);
	in = fopen(errPath, "r");
	if(in == NULL)	errProc("fopen");

	fputs(statusLine, fp);
	fputs(responseHeader, fp);
	fputs(entityHeader, fp);
	fputs(connectType, fp);
	fputs("\r\n", fp);

	while(1)
	{
		c = fgetc(in);
		if(feof(in))	break;
		fputc(c, fp);
	}
	fflush(fp);
	fclose(in);
	fclose(fp);
}
