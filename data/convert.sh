#!/bin/bash

for FILE in `find * -type f | egrep "\.(tra|val|tes|tra_val)$"`
do
	sed -e 's% %,%g' "$FILE" > "$FILE".csv
	java -cp weka.jar weka.core.converters.CSVLoader "$FILE".csv -H -L "att39:A.tomentosum,C.brasiliensis,M.guianensis,M.rubiginosa,P.ramiflora,P.torta" > "$FILE".arff
done

