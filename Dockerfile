FROM openjdk:13-jdk-alpine3.10
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add bind-tools
RUN mkdir -p /usr/local/walletapi
ADD ./target/wallet-api-0.0.1-SNAPSHOT.jar /usr/local/walletapi/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh





## Add Maintainer Info
#LABEL maintainer="Leslie Okoduwa <leslieokoduwa@gmail.com>"
#
## Add volume pointing to /tmp
#VOLUME /tmp
#
## The application's jar file
#ARG JAR_FILE
#
## Add the application's jar to the container
#COPY ${JAR_FILE} app.jar
#
## Execute the application
#ENTRYPOINT ["java", "-jar", "/app.jar"]


##stage 1
##Start with a base image containing Java runtime
#FROM openjdk:11-slim as build
#
## Add Maintainer Info
#LABEL maintainer="Leslie Okoduwa <leslieokoduwa@gmail.com>"
#
## The application's jar file
#ARG JAR_FILE
#
## Add the application's jar to the container
#COPY ${JAR_FILE} app.jar
#
##unpackage jar file
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)
#
##stage 2
##Same Java runtime
#FROM openjdk:11-slim
#
##Add volume pointing to /tmp
#VOLUME /tmp
#
##Copy unpackaged application to new container
#ARG DEPENDENCY=/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#
##execute the application
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.thoughtmechanix.licensingservice.EagleEyeLicensingServiceApplication"]