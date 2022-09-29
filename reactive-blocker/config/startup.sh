#!/bin/bash
nohup /opt/datadog-agent/bin/agent/agent start &>/dev/null &
java $JAVA_OPTS -jar /app/app.jar