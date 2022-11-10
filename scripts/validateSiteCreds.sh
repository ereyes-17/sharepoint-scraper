#! /bin/bash

username=$1
password=$2
domain=$3

siteList=$4

while read line; do
    echo "Checking $line/_api/Web..."
    curl -s -o /dev/null -w "%{http_code}" $line/_api/Web --ntlm -u "$domain\\$username:$password" -H "X-FORMS_BASED_AUTH_ACCEPTED:f"
    echo
done < $siteList