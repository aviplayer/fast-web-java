FROM azul/zulu-openjdk-alpine:17-latest

COPY ./app.jar app.jar
EXPOSE 8089
CMD ["/bin/sh", "-c", "java -XX:+UseParallelGC -Xmx52m -jar app.jar"]
