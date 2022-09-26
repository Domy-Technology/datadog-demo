FROM eclipse-temurin:17

WORKDIR /app

ARG JAR_FILE
COPY build/libs/datadog-demo-0.0.1-SNAPSHOT.jar app.jar
COPY config/* .

RUN chmod +x startup.sh

RUN apt-get install wget curl apt-transport-https 
RUN DD_AGENT_MAJOR_VERSION=7 DD_API_KEY=07dd098280c741d1e0f8b19587d8f5ce DD_SITE="datadoghq.com" bash -c "$(curl -L https://s3.amazonaws.com/dd-agent/scripts/install_script.sh)"
RUN wget -O dd-java-agent.jar https://dtdg.co/latest-java-tracer

ENV JAVA_OPTS="-javaagent:/app/dd-java-agent.jar -Ddd.agent.port=8126 -Ddd.agent.host=datadog-agent -Ddd.profiling.enabled=true -XX:FlightRecorderOptions=stackdepth=256 -Ddd.logs.injection=true -Ddd.service=datadog-demo-api -Ddd.env=dev -Ddd.version=1.0 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7489 -Dcom.sun.management.jmxremote.rmi.port=12349 -Dcom.sun.management.jmxremote.ssl=false -XX:+ShowCodeDetailsInExceptionMessages -Dspring.jmx.enabled=true -Dspring.application.admin.enabled=true -Dspring.boot.project.name=datadog-demo -Djava.rmi.server.hostname=localhost -Ddd.appsec.enabled=true -Xms256M -Xmx1G"
ENV DD_API_KEY=07dd098280c741d1e0f8b19587d8f5ce
ENV DD_ENV=prd
ENV DD_SERVICE=datadog-demo-api
ENV DD_SITE=datadoghq.com
ENV DD_APM_ENABLED=true
ENV DD_AGENT_HOST=datadog-agent

RUN mkdir /etc/datadog-agent/conf.d/java.d
RUN mv java.conf.yaml /etc/datadog-agent/conf.d/java.d/conf.yaml
RUN mv jmx.conf.yaml /etc/datadog-agent/conf.d/jmx.d/conf.yaml
RUN mv security-agent.yaml /etc/datadog-agent/security-agent.yaml
RUN mv system-probe.yaml /etc/datadog-agent/system-probe.yaml
RUN mv datadog.yaml /etc/datadog-agent/datadog.yaml

RUN chmod -R 777 /etc/datadog-agent

EXPOSE 8080

ENTRYPOINT exec /app/startup.sh