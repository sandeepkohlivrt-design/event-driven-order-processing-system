# Event-Driven Order Processing System

A portfolio project demonstrating asynchronous order processing with **Java, Spring Boot, Kafka, PostgreSQL, Redis, and Docker**.

## What it does

- Accepts new orders through a REST API
- Stores order data in PostgreSQL
- Publishes order-created events to Kafka
- Processes events asynchronously
- Prevents duplicate event handling with an idempotency table
- Retries failed Kafka records and sends exhausted records to a dead-letter topic
- Caches order lookups in Redis
- Exposes health and metrics endpoints with Spring Boot Actuator

## Architecture

1. `POST /api/orders` creates an order in PostgreSQL.
2. The application publishes an `ORDER_CREATED` event to Kafka.
3. A Kafka consumer validates the order and moves it through processing states.
4. Processed event IDs are saved so duplicate messages are ignored.
5. `GET /api/orders/{id}` returns the latest order state and uses Redis caching.

## Tech stack

- Java 21
- Spring Boot 3
- Spring Kafka
- PostgreSQL
- Redis
- Docker and Docker Compose
- Maven
- JUnit 5

## Run with Docker

```bash
docker compose up --build
```

Health check:

```text
http://localhost:8080/actuator/health
```

## Create an order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"customer-101","amount":149.99}'
```

Copy the returned `id`, wait briefly for Kafka processing, then retrieve it:

```bash
curl http://localhost:8080/api/orders/<order-id>
```

## Main reliability features

- Kafka-based asynchronous processing
- Idempotent event consumption
- Three retry attempts before dead-letter routing
- PostgreSQL persistence
- Redis caching
- Input validation and centralized error responses

## Repository structure

```text
src/main/java/com/sandeep/orders
├── config
├── controller
├── dto
├── event
├── exception
├── model
├── repository
└── service
```

## Resume-ready description

Built an event-driven order processing system using Java, Spring Boot, Kafka, PostgreSQL, Redis, and Docker. Implemented asynchronous event handling, idempotent consumers, retry and dead-letter processing, persistent order-state tracking, and cached order lookups.
