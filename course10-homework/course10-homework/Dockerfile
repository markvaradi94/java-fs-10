FROM eclipse-temurin:17-jammy

WORKDIR /app

COPY resolv.conf /etc/resolv.conf
COPY build/libs/trains-app.jar /app/trains-app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/trains-app.jar"]
