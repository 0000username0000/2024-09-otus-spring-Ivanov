# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл приложения
COPY build/libs/*.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]