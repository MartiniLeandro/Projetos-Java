FROM container-registry.oracle.com/graalvm/native-image:21-ol9 AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -Pnative -DskipTests

FROM oraclelinux:9-slim
WORKDIR /app

COPY --from=build /app/target/demo ./app-nativo

EXPOSE 8080

CMD ["./app-nativo", "-Dserver.port=${PORT:8080}"]