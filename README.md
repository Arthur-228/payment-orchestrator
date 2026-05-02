# Payment Orchestrator (Conductor)

Центральный сервис-оркестратор платежей для платформы интернет-эквайринга.

## О проекте

`Conductor` отвечает за координацию всего жизненного цикла платежа:
приём запроса, проверку идемпотентности, маршрутизацию, управление состоянием, взаимодействие с внешними системами и
отправку уведомлений.

Проект разработан с акцентом на:

- Высокую надёжность и устойчивость к сбоям
- Многоуровневую идемпотентность
- Чёткое управление состояниями (State Machine)
- Observability и traceability

## Основные возможности

- Идемпотентная обработка платежей
- Статусная машина с валидацией переходов
- Подготовка к асинхронной оркестрации через Kafka (Outbox Pattern)
- Resilience patterns (готовность к Circuit Breaker, Retry)

## Технологический стек

- Java 17, Spring Boot 2.7.18
- Spring Data JPA + Hibernate
- PostgreSQL, Redis, Redpanda (Kafka)
- Docker + Docker Compose

## Как запустить проект локально

### 1. Запуск инфраструктуры

```bash
docker-compose up -d
```

### 2. Запуск приложения

```bash
./gradlew bootRun
```

## API

### Инициация платежа

**POST** `/api/v1/payments/initiate`

**Headers:**
```http
Content-Type: application/json
```

**Request Body:**
```json
{
  "idempotencyKey": "order-12345",
  "merchantId": "merchant-001",
  "amount": 2990.00,
  "currency": "RUB"
}
```

**Response (200 OK):**
```json
{
  "paymentId": "c3a7936e-3e80-40a8-912d-c59e7dc4b999",
  "status": "PENDING",
  "new": true
}
```

**Response при повторном запросе (idempotency):**
```json
{
  "paymentId": "c3a7936e-3e80-40a8-912d-c59e7dc4b999",
  "status": "PENDING",
  "new": false
}
```

### Структура проекта

- domain/ — доменная модель и бизнес-логика
- orchestration/ — ядро оркестратора и статусная машина
- idempotency/ — механизмы идемпотентности
- outbox/ — реализация Outbox Pattern
- api/ — REST-контроллеры и DTO
- infrastructure/ — техническая инфраструктура (Kafka, Redis, конфигурация)


### Документация

- State Machine — переходы состояний платежа
- Architecture Decision Records — ключевые решения

