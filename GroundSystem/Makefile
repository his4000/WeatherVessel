copy:
	scp Server.c pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp Server.h pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp curlNaver.c pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp curlNaver.h pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp fileio.c pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp query.c pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp query.h pi@203.252.166.1:~/CapstoneDesign/GroundSystem
	scp Makefile pi@203.252.166.1:~/CapstoneDesign/GroundSystem
build:
	gcc -o server Server.c curlNaver.c query.c fileio.c -lmysqlclient -lcurl
