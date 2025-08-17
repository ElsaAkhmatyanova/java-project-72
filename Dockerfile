FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY /app .

RUN ["./gradlew", "clean", "build"]

EXPOSE 8080

CMD ["java", "-jar", "build/libs/app-1.0-SNAPSHOT-all.jar"]
