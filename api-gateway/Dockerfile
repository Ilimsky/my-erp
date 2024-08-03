# Используем базовый образ Java
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт
EXPOSE 8090

# Запускаем приложение
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]