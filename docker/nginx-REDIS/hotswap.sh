#!/usr/bin/env bash
NEWDB=$1
COMM="/usr/sbin/nginx -s reload"

if grep -q "$NEWDB:6379" /etc/nginx/nginx.conf; then
    echo "Already connected to $NEWDB!"
else
    sed -i "s/server\s.*\:/server $NEWDB:/g" /etc/nginx/nginx.conf
	eval $COMM
fi
