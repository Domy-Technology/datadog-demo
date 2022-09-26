# datadog-demo

## Run Server

```shell
cd api
./gradlew clean build
java -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=7489 -XX:+ShowCodeDetailsInExceptionMessages -Dspring.jmx.enabled=true -Dspring.application.admin.enabled=true -Dspring.boot.project.name=datadog-demo -Djava.rmi.server.hostname=localhost -jar build/libs/datadog-demo-0.0.1-SNAPSHOT.jar

java -XX:+ShowCodeDetailsInExceptionMessages -Dspring.jmx.enabled=true -Dspring.application.admin.enabled=true -Dspring.boot.project.name=datadog-demo -Djava.rmi.server.hostname=localhost -jar build/libs/datadog-demo-0.0.1-SNAPSHOT.jar
```

## Database

```
User: jschmidt
Pass: n0JTV6TKTliuKeen

```

## Azure Data

```json
{
  "appId": "affe47c4-70b4-4968-ab96-8ce692a2bd42",
  "displayName": "azure-cli-2022-09-25-01-36-54",
  "password": "rJ18Q~wkibAQBkhpBHcGEW8yijeGUx3LOzv6Fb96",
  "tenant": "54d8183e-93dd-4d0c-ba30-5b931e0649cd"
}
```

## Run With Agent

```
java -javaagent:dd-java-agent.jar -Ddd.profiling.enabled=true -XX:FlightRecorderOptions=stackdepth=256 -Ddd.logs.injection=true -Ddd.service=datadog-demo-api -Ddd.env=dev -Ddd.version=1.0 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7489 -Dcom.sun.management.jmxremote.rmi.port=12349 -Dcom.sun.management.jmxremote.ssl=false -XX:+ShowCodeDetailsInExceptionMessages -Dspring.jmx.enabled=true -Dspring.application.admin.enabled=true -Dspring.boot.project.name=datadog-demo -Djava.rmi.server.hostname=localhost -Ddd.appsec.enabled=true -Xms256M -Xmx1G -jar api/build/libs/datadog-demo-0.0.1-SNAPSHOT.jar
```