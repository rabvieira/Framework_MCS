#!/bin/bash

for FILENAMETRA in `find new_feat_tra/* -type f`
do
	FILEPATH=`dirname "$FILENAMETRA" | sed -e 's%tra%tra_val%g'`
	if [ ! -e "$FILEPATH" ]
	then
		mkdir -p "$FILEPATH"
	fi
	FILENAMEVAL=`echo "$FILENAMETRA" | sed -e 's%tra%val%g'`
	FILENAMETRAVAL=`echo "$FILENAMETRA" | sed -e 's%tra%tra_val%g'`
	cat "$FILENAMETRA" "$FILENAMEVAL" > "$FILENAMETRAVAL"
done
