#!/bin/bash

#
# Run SEL interpreter
#

# set config environment variables
source cprl_config.sh

# Update CLASSPATH for SEL
SEL_HOME=$WORKSPACE_HOME/SEL
export CLASSPATH=$SEL_HOME/$CLASSES_DIR:$CLASSPATH

java -ea edu.citadel.sel.Interpreter $1
