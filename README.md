# Приложение для перевода текста

## Описание
Веб-приложение для перевода текста при помощи API Yandex.Translate

## Запуск приложения
1. Склонируйте репозиторий 
```bash
git clone https://github.com/prostotema1/Translator.git
```

2. Перейдите в директорию с проектом
3. Поменяйте параметры для пользователя базы данных в resources/application.properties
4. Зарегистрируйте аккаунт в Yandex.Cloud по [документации](https://yandex.cloud/ru/docs/translate/quickstart).
5. Получите свой IAM-токен и проставьте его в поле yandex.api.token в applitcation.properties
6. Получите свой folderId и проставьте его в application.properties
7. Запустите приложение
```bash
mvn spring-boot:run
```

## Использование

Для использования приложения необходимо отправить POST-запрос на /api/translate с телом запроса в формате JSON, например:
```bash
curl -X http://localhost:8080/api/translate -H "Content-type: application/json" -d '{"inputText":"Hello world!","sourceLanguage":"en","targetLanguage":"ru"}'
```