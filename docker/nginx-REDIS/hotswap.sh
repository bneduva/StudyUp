#!/usr/bin/env bash
NEWDB=$1
COMM="/usr/sbin/nginx -s reload"

sed -i "s/server\s.*\:/server $NEWDB:/g" /etc/nginx/nginx.conf
eval $COMM
