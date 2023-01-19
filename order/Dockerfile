FROM maven:3.6.3-openjdk-17-slim as build
COPY . .
RUN mvn clean package -T10
COPY target/order-1.0.0.jar order-1.0.0.jar
ENTRYPOINT ["java","-jar","/order-1.0.0.jar"]
