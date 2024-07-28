# Используем Debian как базовый образ для среды сборки
FROM debian:latest AS build-env

# Устанавливаем все необходимые пакеты
RUN apt-get update
RUN apt-get install -y curl git unzip

# Определяем переменные
ARG FLUTTER_SDK=/usr/local/flutter
ARG FLUTTER_VERSION=3.22.2
ARG APP=/app/

# Клонируем репозиторий Flutter
RUN git clone https://github.com/flutter/flutter.git $FLUTTER_SDK
# Переходим в каталог Flutter и переключаемся на указанную версию
RUN cd $FLUTTER_SDK && git fetch && git checkout $FLUTTER_VERSION

# Настраиваем путь к Flutter как переменную окружения
ENV PATH="$FLUTTER_SDK/bin:$FLUTTER_SDK/bin/cache/dart-sdk/bin:${PATH}"

# Запускаем команды Flutter
# Проверяем установку Flutter
RUN flutter doctor -v

# Создаем каталог для копирования исходного кода
RUN mkdir $APP
# Копируем исходный код в каталог
COPY . $APP
# Устанавливаем рабочий каталог
WORKDIR $APP

# Запускаем сборку: 1 - очистка, 2 - получение зависимостей, 3 - сборка веб-приложения
RUN flutter clean
RUN flutter pub get
RUN flutter build web

# На этом этапе приложение будет собрано и готово к деплою

# Используем nginx для деплоя
FROM nginx:1.25.2-alpine

# Копируем данные собранного веб-приложения в nginx
COPY --from=build-env /app/build/web /usr/share/nginx/html

# Открываем порт и запускаем nginx
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]