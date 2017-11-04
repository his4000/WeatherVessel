copy:
	scp Server.c Server.h curlNaver.c curlNaver.h fileio.c query.c query.h Makefile pi@192.168.0.10:~/CapstoneDesign/GroundSystem
build:
	gcc -o server Server.c curlNaver.c query.c fileio.c -lmysqlclient -lcurl
