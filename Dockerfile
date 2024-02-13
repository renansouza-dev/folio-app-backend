# (1)
FROM alpine:latest AS build
ENV JAVA_HOME /opt/jdk/jdk-21.0.1+12
ENV PATH $JAVA_HOME/bin:$PATH

# (2)
ADD https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.1%2B12/OpenJDK21U-jdk_x64_alpine-linux_hotspot_21.0.1_12.tar.gz /opt/jdk/
RUN tar -xzvf /opt/jdk/OpenJDK21U-jdk_x64_alpine-linux_hotspot_21.0.1_12.tar.gz -C /opt/jdk/

# (3)
RUN ["jlink", "--compress=2", \
     "--module-path", "/opt/jdk/jdk-21.0.1/jmods/", \
# (4)
     "--add-modules", "java.base,java.logging,java.naming,java.desktop,jdk.unsupported,java.management,java.security.jgss,java.instrument,java.sql", \
     "--no-header-files", "--no-man-pages", \
     "--output", "/springboot-runtime"]

# (5)
FROM alpine:latest
# (6)
COPY --from=build  /springboot-runtime /opt/jdk
ENV PATH=$PATH:/opt/jdk/bin
EXPOSE 8080
COPY ../target/*.jar app.jar
CMD ["java", "-showversion", "-jar", "/app/app.jar"]
