#!/bin/sh
JAR_PATH=$1
mvn install:install-file -Dfile=$JAR_PATH -DgroupId=com.opencv -DartifactId=opencv -Dversion=3.4.1 -Dpackaging=jar