FROM maven:3.9.0-amazoncorretto-19 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
#COPY . .
#RUN mvn clean package -DskipTests

#
# Package stage
#
FROM amazoncorretto:19-alpine3.17-jdk
#COPY --from=build /target/jl_entities-0.0.1-SNAPSHOT.jar app.jar
## ENV PORT=8080
#EXPOSE 8090
#ENTRYPOINT ["java","-jar","app.jar"]
COPY --from=build /home/app/target/livraison-app-backend-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]