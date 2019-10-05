#!/bin/bash

#
# Run CPRL TestParser on a single ".cprl" file
#

# set config environment variables
source cprl_config.sh

java -ea test.cprl.TestParser $1
