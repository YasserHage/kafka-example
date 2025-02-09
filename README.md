# Kafka Notification Service

## Overview

This project consists of two modules:

- **Producer Module**: Responsible for sending notifications to a Kafka topic.
- **Consumer Module**: Listens for notifications from the Kafka topic and processes them.

The system uses **Spring Boot**, **Spring Kafka**, and **Retryable Topics** to handle message processing and failures.

## Modules

### 1. Producer Module

#### Key Components:

- **`NotificationController`**: REST endpoints to send notifications.
- **`NotificationService`**: Sends messages to Kafka.

#### Endpoints:

- **Send a single message:**
  ```http
  POST /notification/send
  ```
  **Request Body:**
  ```json
  {
    "id": "123",
    "message": "Test Notification"
  }
  ```
- **Send bulk messages:**

  Sends the same message 100 times to a Kafka topic across multiple IDs, providing a way to visualize the parallelism and message ordering within Kafka partitions.
  ```http
  POST /notification/bulk/{repeat}
  ```
  **Path Variable:** `repeat` → Number of IDs that will be used to repeat the message.&#x20;

  **Request Body:** A simple message string.

### 2. Consumer Module

#### Key Components:

- **`NotificationConsumer`**: Listens for messages from Kafka and processes them.
- **Retry Mechanism**: If a message has a key of `error-test`, an exception is thrown, triggering retries.
- **Error Handling & Retry Strategy**:
    - Messages with key `error-test` simulate processing failures.
    - Such messages are retried **3 times** with an **exponential backoff** (1s → 2s → 4s).
    - If all retries fail, the message is sent to a **Dead Letter Topic (DLT)** and processed by `NotificationDltConsumer`.

### Prerequisites

- Java 21+
- Apache Kafka running locally
- Maven

### Steps to Run

1. **Start Kafka**

   ```sh
   docker pull apache/kafka:3.9.0
   docker run -p 9092:9092 apache/kafka:3.9.0
   ```

2. **Run the Producer Module**

   ```sh
   mvn spring-boot:run -pl producer
   ```

3. **Run the Consumer Module**

   ```sh
   mvn spring-boot:run -pl consumer
   ```

   > **Note:** The consumer runs on a default port  8080. If needed, modify the `server.port` property in `application.yml` or override it at runtime:

   ```sh
   mvn spring-boot:run -pl consumer -Dspring-boot.run.arguments=--server.port=9091
   ```

4. **Send a Test Notification**

   ```sh
   curl -X POST -H "Content-Type: application/json" \
        -d '{"id": "1", "message": "Hello, Kafka!"}' \
        http://localhost:8080/notification/send
   ```

## License

This project is licensed under the MIT License.

