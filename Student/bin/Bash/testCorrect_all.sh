#!/bin/bash

#
# Run testCorrect on all ".obj" files in the current directory
#

for file in *.obj
do
   filename=$(basename $file)
   filename="${filename%.*}"
   testCorrect.sh $filename
done
