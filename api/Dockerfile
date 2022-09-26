FROM openjdk-17-jdk

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN DD_AGENT_MAJOR_VERSION=7 DD_API_KEY=cb64cb9a94020060b56231d26a38ee7b DD_SITE="datadoghq.com" bash -c "$(curl -L https://s3.amazonaws.com/dd-agent/scripts/install_script.sh)"
RUN wget -O dd-java-agent.jar https://dtdg.co/latest-java-tracer

ENV JAVA_OPTS="-javaagent:dd-java-agent.jar -Ddd.profiling.enabled=true -XX:FlightRecorderOptions=stackdepth=256 -Ddd.logs.injection=true -Ddd.service=datadog-demo-api -Ddd.env=dev -Ddd.version=1.0 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7489 -Dcom.sun.management.jmxremote.rmi.port=12349 -Dcom.sun.management.jmxremote.ssl=false -XX:+ShowCodeDetailsInExceptionMessages -Dspring.jmx.enabled=true -Dspring.application.admin.enabled=true -Dspring.boot.project.name=datadog-demo -Djava.rmi.server.hostname=localhost -Ddd.appsec.enabled=true -Xms256M -Xmx1G"
ENV DD_API_KEY=07dd098280c741d1e0f8b19587d8f5ce
ENV DD_ENV=prd
ENV DD_SERVICE=datadog-demo-api
ENV DD_SITE=datadoghq.com

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]