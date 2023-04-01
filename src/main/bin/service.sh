#!/bin/bash

set -e

LC_LANG=zh_CN.UTF-8
LC_ALL=zh_CN.UTF-8
export LC_LANG
export LC_ALL
ulimit -n 65535

#Gets JAVA_HOME

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        RUN_JAVA="$JAVA_HOME/jre/sh/java"
    else
        RUN_JAVA="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$RUN_JAVA" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    RUN_JAVA="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

echo "Using JAVA_HOME：        $RUN_JAVA"

#Gets the actual path of the service.sh

PRG="$0"

while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' >/dev/null; then
    PRG="$link"
  else
    PRG=$(dirname "$PRG")/"$link"
  fi
done

PRG_HOME=$(
  cd "$(dirname "$PRG")"
  pwd
)

echo "Program home is         $PRG_HOME"

echo "-----------------------------------------------"

# set some common jvm config

sed 's/\r//g' -i "$PRG_HOME"/service.vmoptions
JAVA_OPTS=$(cat "$PRG_HOME"/service.vmoptions)

export START_JAR_NAME="*.jar"

START_JAR=$(ls $PRG_HOME | grep $START_JAR_NAME)


start() {
  echo $"Starting :"
  eval \"$RUN_JAVA\" $JAVA_OPTS -jar $START_JAR &
}

debug() {
  echo $"If you use DEBUG MODE,You must stop Server before do debug"
  echo -n $"debug port:5050"
  echo $"Starting debug: "
  eval \"$RUN_JAVA\" $JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=5050,server=y,suspend=y -jar $START_JAR
}

console() {
  echo $"Starting : "
  eval \"$RUN_JAVA\" $JAVA_OPTS -jar $START_JAR
}

stop() {

    echo  $"Stoping : "
    boot_id=$(cat  $PRG_HOME/application.pid)
    echo "bootid : $boot_id"
    count=$(ps -ef | grep $boot_id | wc -l)
    echo "count: $count"
    if [ $count -gt 1 ]; then
         kill  $boot_id
         sleep 2
         count=$(ps -ef | grep $boot_id | wc -l)
         if [ $count -gt 1 ]; then
           kill -9 $boot_id
         else
           echo "Application closed..."
         fi
    else
     echo  "Application not running..."
    fi
}

if [ "" = "$1" ] ;then
    start
else
  case $1 in
  start)
  start
  ;;
  debug)
  debug
  ;;
  stop)
  stop
  ;;
  restart)
  stop
  sleep 5
  echo $"Restarting"
  start
  ;;
  console)
  console
  ;;
  help)
  exit 1
  ;;
  *)
  echo $"Usage: $1  ( commands ... )"
  exit 1
  ;;
  esac
fi

exit 0