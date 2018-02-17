#!/bin/bash
if [ -z "$1" ]
then
    echo "usage: ./scripts/download_schema.sh URL DEST FILENAME"
    exit 1
fi
URL=$1
DEST=$2
FILENAME=$3
echo $DEST
mkdir -p ${DEST}
curl -o "${DEST}/${FILENAME}" ${URL}

