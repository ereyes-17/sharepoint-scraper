#! /bin/bash

USERNAME=$1
PASSWORD=$2
DOMAIN=$3
SITES_FILE=$4
PATH_TO_JAR=$5

if [ $USERNAME -eq "-h" ]; then
	echo "HOW TO USE: "
	echo "/path/to/script <username> <password> <domain> <site_list_file> <path_to_jar>"
	exit
fi

JAR_OPTS="--skip=subsites,lists,folders,recycled,groups --no-download --output=true"

while read line; do
	java -jar $PATH_TO_JAR $USERNAME $PASSWORD $DOMAIN $JAR_OPTS
	echo "-------------------- NEXT RUN --------------------"
done < $SITE_FILE
