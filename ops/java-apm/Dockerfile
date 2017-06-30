FROM openjdk:8-alpine

RUN apk --no-cache add curl
RUN apk --no-cache add unzip

ADD "https://github.com/glowroot/glowroot/releases/download/v0.9.20/glowroot-central-0.9.20-dist.zip" /

RUN unzip glowroot-central-0.9.20-dist.zip
RUN rm -rf glowroot-central-0.9.20-dist.zip

ADD resources/glowroot-central.properties /

EXPOSE 8181
EXPOSE 4000

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Dhttp.nonProxyHosts=localhost -jar /glowroot-central/glowroot-central.jar"]

HEALTHCHECK --interval=5s --timeout=3s --retries=10 CMD curl --fail http://localhost:4000 || exit 1
