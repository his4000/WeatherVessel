#include</usr/include/mysql/mysql.h>
#include<string.h>
#include<stdio.h>

#include"query.h"
/*
* This component needs to mysqlclient library.
*
* If you don't have mysqlclient library,
* you have to download library, first.
*
* If you want to compile this component,
* you should add -lmysqlclient.
*/
int sendQuery(char* query){
	MYSQL*      connection = NULL, conn;
	MYSQL_RES*  sql_result;
	MYSQL_ROW   sql_row;
	int         query_stat;

	int         drone_id;
	char        drone_name[255];

	mysql_init(&conn);

	connection = mysql_real_connect(&conn, DB_HOST,
									DB_USER, DB_PASS,
									DB_NAME, 3306,
									(char*)NULL, 0);
	if(connection == NULL){
		fprintf(stderr, "Mysql connection error : %s", mysql_error(&conn));
		return 1;
	}

	query_stat = mysql_query(connection, query);
	if(query_stat != 0){
		fprintf(stderr, "Mysql query error : %s", mysql_error(&conn));
		return 1;
	}

	mysql_close(connection);
	printf("Success to send query\n");

	return 0;
}
