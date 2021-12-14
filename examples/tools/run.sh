#! /bin/sh

if [ ! -r target/classpath.txt ]; then
    # Get classpath (including dependencies) from Maven
    mvn -q -Dmdep.outputFile=target/classpath.txt dependency:build-classpath
fi

java -cp target/classes:"$(cat target/classpath.txt)" \
    tools.Main "$@"
