ARG JDK_VERSION=11.0.9
FROM openjdk:${JDK_VERSION}-slim as app

EXPOSE 7050

WORKDIR /app

ARG ARG_VERSION
ENV VERSION=${ARG_VERSION}

COPY target/container-${VERSION}.jar container-${VERSION}.jar

ENTRYPOINT ["sh", "-c", "exec java -cp container-${VERSION}.jar -Xmx4g io.castled.CastledApplication castled-server configs/config.yml"]
