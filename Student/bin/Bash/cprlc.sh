#!/bin/bash

#
# Run CPRL compiler on a single ".cprl" file
#

# set config environment variables
source cprl_config.sh

java -ea edu.citadel.cprl.Compiler $1
