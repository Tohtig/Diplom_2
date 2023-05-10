# API тесты
API тесты для [приложения-песочницы](https://stellarburgers.nomoreparties.site). 
[Документация](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf) на API приложения.

1. В maven подключены библиотеки: **JUnit 4 + JUnitParams, Allure, rest-assured, gson, javafaker, lombok**. Настроен на работу с Java 11.
2. Нужные тестовые данные создаются перед тестом и удаляются после того, как он выполнится. 
3. В репозиторий добавлен формируемый Allure-отчёт.