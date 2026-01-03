# always dockerfile starts with FROM instruction

FROM eclipse-temurin:21

LABEL maintainer="omarellafy1@gmail.com"

WORKDIR /app

COPY target/ecommerce-rest-api-0.0.1-SNAPSHOT.jar /app/springboot-ecommerce-rest-api.jar

ENTRYPOINT ["java", "-jar", "springboot-ecommerce-rest-api.jar"]