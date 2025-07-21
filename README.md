# Структура Папок

Local/                            # Главный модуль приложени
│
├── LocalApplication.java         # Точка входа Spring Boot приложения
│
├── CustomerContext/             # Bounded Context "Customer"
│   │
│   ├── application/             # Application Layer
│   │   ├── applicationService/  # Сервисы, управляющие use case'ами
│   │   ├── DTO/                 # Data Transfer Object'ы
│   │   ├── exceptions/          # Пользовательские исключения
│   │   ├── handlers/            # Обработчики (например, exception handler'ы)
│   │   └── mapper/              # MapStruct или ручные мапперы DTO ↔ Entity
│   │
│   ├── domain/                  # Domain Layer
│   │   ├── event/               # Доменные события
│   │   ├── model/               # Модели домена: агрегаты, сущности, value object'ы
│   │   ├── repository/          # Интерфейсы репозиториев
│   │   └── service/             # Доменные сервисы (если логика не в сущностях)
│   │
│   ├── infrastructure/         # Infrastructure Layer
│   │   ├── configuration/      # Конфигурация Spring, Beans и т.п.
│   │   ├── messaging/          # Механизмы событий или взаимодействия между сервисами
│   │   ├── migration/          # Скрипты миграции (напр. Flyway или Liquibase)
│   │   └── security/           # Безопасность, фильтры, авторизация
│   │
│   └── presentation/           # Web Layer (контроллеры)
│       ├── CustomerCartController.java
│       ├── CustomerCommentController.java
│       ├── CustomerController.java
│       ├── CustomerFavoritesController.java
│       └── CustomerQueryController.java
│
├── shared/                      # Shared Kernel (повторно используемый код)
│   ├── domain/                 
│   │   ├── valueObject/         # Общие value object'ы (например, Email, Address)
│   │   ├── AggregateRoot.java   # Базовый класс агрегата
│   │   ├── BaseEntity.java      # Базовый класс сущности
│   │   ├── DomainEvent.java     # Интерфейс или базовый класс доменного события
│   │   ├── DomainEventPublisher.java  # Интерфейс/реализация публикации событий
│   │   └── ValueObject.java     # Интерфейс для Value Object
│   │
│   └── infrastructure/
│       ├── configuration/       # Общая конфигурация
│       └── exception/           # Глобальные исключения и обработчики
