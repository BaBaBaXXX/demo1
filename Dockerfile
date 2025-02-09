FROM amazoncorretto:21-al2023-headless
WORKDIR /app
COPY build/libs/reminder*.jar /app/reminder.jar
COPY .env /app/.env
CMD ["java", "-jar", "reminder.jar"]