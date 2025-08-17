FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY /app .

RUN ["./gradlew", "clean", "build"]

EXPOSE 8080

CMD ["./gradlew", "run"]