# Smart-Expense-Tracker-API

## Overview

Expense Tracker REST API is a RESTful backend application for managing daily expenses. It provides endpoints to create, retrieve, filter, calculate totals, and delete expenses, with request validation and consistent error handling.

The application is organized into controller, service, and repository layers. Request and response DTOs define the API contract, while repository abstractions separate business logic from the underlying data storage.

The current implementation uses an in-memory repository for persistence. This keeps the project lightweight while allowing the persistence layer to be replaced by a database implementation with minimal changes to the rest of the application.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Architecture](#architecture)
- [Design Decisions](#design-decisions)
- [Assumptions](#assumptions)
- [Future Improvements](#future-improvements)

## Technology Stack

| Technology | Purpose |
|------------|---------|
| **Java 21** | Primary programming language used to implement the application. |
| **Spring Boot 4** | Provides the REST API framework and dependency injection. |
| **Maven** | Manages dependencies and automates the build process. |
| **Jakarta Validation** | Validates incoming request payloads before business logic is executed. |
| **Lombok** | Reduces boilerplate code by generating constructors, getters, setters, and other utility methods. |
| **Docker** | Packages the application into a portable container for consistent deployment across environments. |
| **JUnit 5** | Used for unit and integration testing. |
| **Mockito** | Used to mock dependencies during unit and controller testing. |


## Getting Started

### Prerequisites

Ensure the following tools are installed before running the application.

| Tool | Required | Download | Verify Installation |
|------|:--------:|----------|---------------------|
| Java 21 (JDK) | Yes | [Eclipse Temurin](https://adoptium.net/) | `java --version` |
| Git | Yes | [Git SCM](https://git-scm.com/downloads) | `git --version` |
| Docker | Optional | [Docker](https://www.docker.com/get-started/) | `docker --version` |

> **Note:** The project includes the Maven Wrapper (`mvnw`), so a separate Maven installation is **not required**.

### Run Locally

**1. Clone the repository**

```bash
git clone https://github.com/Akash3206/smart-expense-tracker-api.git
```

**2. Navigate to the project directory**

```bash
cd smart-expense-tracker-api
```

**3. Build the project and download dependencies**

> **Linux / macOS**

```bash
./mvnw clean install
```

> **Windows**

```cmd
mvnw.cmd clean install
```

**4. Start the application**

> **Linux / macOS**

```bash
./mvnw spring-boot:run
```

> **Windows**

```cmd
mvnw.cmd spring-boot:run
```
> This command blocks the terminal while the application is running. Leave it running and open a new terminal window to execute the verification step below.

**5. Verify the application is running**

Execute the following command:

```text
curl http://localhost:8080/api/expenses
```

If the application starts successfully, the endpoint returns an empty JSON array (`[]`) when no expenses have been created.

---

### Run with Docker

**1. Build the Docker image**

```bash
docker build -t expense-tracker-api .
```

**2. Start the container**

```bash
docker run -p 8080:8080 expense-tracker-api
```

**3. Verify the application is running**

```text
curl http://localhost:8080/api/expenses
```
If the application starts successfully, the endpoint returns an empty JSON array (`[]`) when no expenses have been created.

## Features

- Create, retrieve, filter, and delete expense records through RESTful API endpoints.
- Calculate total expenses across all records or for a specific category.
- Validate incoming requests using Jakarta Validation with descriptive error responses.
- Centralized exception handling for consistent HTTP error responses.
- Layered architecture with separate controller, service, and repository responsibilities.
- DTO-based API contract to separate request and response models from the domain model.
- In-memory repository implementation with repository abstraction for future database integration.
- Docker support for consistent application deployment across environments.
- Comprehensive automated test suite covering unit, controller, and integration scenarios.

## API Endpoints

**Base URL**

```text
http://localhost:8080
```

### Endpoints

| Method | Endpoint | Description |
|:------:|----------|-------------|
| `POST` | `/api/expenses` | Create a new expense. |
| `GET` | `/api/expenses` | Retrieve all expenses. |
| `GET` | `/api/expenses/category/{category}` | Retrieve all expenses for a specific category. |
| `GET` | `/api/expenses/total` | Calculate the total amount of all expenses. |
| `GET` | `/api/expenses/total/{category}` | Calculate the total amount for a specific category. |
| `DELETE` | `/api/expenses/{id}` | Delete an expense by its ID. |

---

### Request Validation

The `POST /api/expenses` endpoint validates incoming requests before processing them.

| Field | Validation |
|-------|------------|
| `title` | Required and must not be blank. |
| `amount` | Required and must be greater than `0.00`. |
| `category` | Required and must be a valid category. |
| `date` | Required and must use the ISO-8601 format (`yyyy-MM-dd`). |

---

### Supported Categories

- `FOOD`
- `TRANSPORT`
- `HEALTH`
- `ENTERTAINMENT`
- `SHOPPING`
- `UTILITIES`
- `OTHER`

---

### Error Response

The API returns structured JSON responses for validation failures and application errors.

```json
{
  "timestamp": "2026-08-01T12:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "validationErrors": {
    "title": "Title is required",
    "amount": "Amount must be greater than zero"
  }
}
```

## Testing

The project includes a comprehensive automated test suite covering the application's core functionality, business logic, REST API, and end-to-end request flow.

### Run the Test Suite

Execute the following command from the project root directory to run all automated tests.

> **Linux / macOS**

```bash
./mvnw test
```

> **Windows**

```cmd
mvnw.cmd test
```
---

### Test Coverage


| Test Category | Coverage |
|---------------|----------|
| **Application Context** | Verifies that the Spring Boot application context loads successfully. |
| **Repository Unit Tests** | Validates CRUD operations, category filtering, and repository edge cases. |
| **Service Unit Tests** | Verifies business logic, total calculations, and exception handling. |
| **Controller (Web MVC) Tests** | Tests REST endpoints, request validation, HTTP status codes, and error responses using MockMvc. |
| **Integration Tests** | Verifies complete request flow across the Controller, Service, and Repository layers without mocking application components. |

---

### Integration Scenarios

The integration tests cover the following end-to-end workflows:

- Create an expense and retrieve it successfully.
- Create multiple expenses and verify the calculated total.
- Create an expense, delete it, and verify its removal.
- Attempt to delete a non-existent expense and verify the `404 Not Found` response.

---

### Test Summary

| Category | Count |
|----------|------:|
| Application Context Tests | 1 |
| Repository Unit Tests | 8 |
| Service Unit Tests | 9 |
| Controller (Web MVC) Tests | 15 |
| Integration Tests | 4 |
| **Total Automated Tests** | **37** |
All tests are self-contained and can be executed using a single Maven command without requiring any external services, database setup, or additional configuration.


## Architecture

The application follows a layered architecture where each layer has a well-defined responsibility. Incoming requests are validated and mapped to domain models before reaching the business logic, while responses are converted back into DTOs before being returned to the client.

```text
                    HTTP Request
                          │
                     ── request ──
                          ▼
              CreateExpenseRequest (DTO)
                          │
                          ▼
                 ExpenseController
                          │
                          ▼
           ExpenseService (Interface)
                          │
                          ▼
              ExpenseServiceImpl
                          │
                          ▼
                 ExpenseMapper
                          │
                          ▼
               Expense (Domain Model)
                          │
                          ▼
         ExpenseRepository (Interface)
                          │
                          ▼
        InMemoryExpenseRepository
                          │
                          ▼
             ConcurrentHashMap
                          │
                     ── response ──
                          ▼
               Expense (Domain Model)
                          │
                          ▼
                 ExpenseMapper
                          │
                          ▼
             ExpenseResponse (DTO)
                          │
                          ▼
                    HTTP Response
```
### Project Structure

```text
src/
├── controller/     # REST API endpoints
├── dto/            # Request and response models
├── exception/      # Custom exceptions and global exception handling
├── mapper/         # DTO ↔ Domain object conversion
├── model/          # Domain models and enums
├── repository/     # Data access abstraction and implementation
├── service/        # Business logic
└── tests/          # Unit and integration tests
```

### Layer Responsibilities

| Layer | Responsibility |
|--------|----------------|
| **Controller** | Handles HTTP requests and delegates business operations to the service layer. |
| **Service** | Implements business logic and coordinates interactions between components. |
| **Repository** | Abstracts data access and persistence operations. |
| **DTO** | Defines the API contract for incoming requests and outgoing responses. |
| **Mapper** | Converts between DTOs and domain models. |
| **Exception** | Provides centralized exception handling and consistent error responses. |


## Design Decisions

Most of the design decisions in this project follow a single principle: each component should have a well-defined responsibility so it can be understood, tested, and replaced independently.

### 1. DTO Pattern

Request and response DTOs are used as the API contract instead of exposing domain models directly. This separates the external API from the internal implementation, allowing either to evolve independently without affecting the other.

**Trade-off:** DTOs introduce additional mapping code and require the DTOs, domain model, and mapper to be kept in sync as the application evolves.

---

### 2. Repository Abstraction

The application interacts with the persistence layer through the `ExpenseRepository` interface rather than a concrete implementation. Spring's dependency injection resolves the implementation at runtime, allowing the current in-memory repository to be replaced with a database-backed implementation without changing the service layer.

**Trade-off:** Introducing an abstraction adds a small amount of complexity for a simple project, but it keeps the business logic independent of the persistence mechanism.

---

### 3. In-Memory Storage (`ConcurrentHashMap`)

The project uses an in-memory repository backed by `ConcurrentHashMap` to satisfy the assessment requirements while keeping the application lightweight and free from external database dependencies. `ConcurrentHashMap` was chosen over `HashMap` to safely handle concurrent requests.

**Trade-off:** Data is not persisted across application restarts, making this approach suitable for the scope of the assessment but not for production environments.

---

### 4. `BigDecimal` for Monetary Values

Expense amounts are represented using `BigDecimal` instead of floating-point types such as `double` or `float`. This avoids precision errors that commonly occur when performing financial calculations.

**Trade-off:** `BigDecimal` is more verbose to work with than primitive numeric types, but it provides the precision required for monetary values.

---

### 5. Centralized Exception Handling

Application exceptions are handled using a global exception handler (`@ControllerAdvice`) instead of individual `try-catch` blocks within controllers. This ensures that all endpoints return consistent and predictable error responses.

**Trade-off:** Any new exception type must be explicitly handled by the global exception handler. If it is not registered, it falls back to a generic or unhandled error response, making the handler an important part of ongoing maintenance.

---

### 6. Testing Strategy

The test suite is organized into repository, service, controller, and integration tests. Unit tests isolate individual components using Mockito where appropriate, while controller tests use MockMvc to verify HTTP behavior. Integration tests validate complete request flows across multiple application layers without mocking application components.

**Trade-off:** Maintaining multiple layers of tests requires additional effort, but it provides greater confidence that both individual components and the application as a whole behave correctly.

## Assumptions

The following assumptions were made while implementing this project:

- Expense data is stored in memory and is not expected to persist after the application is stopped or restarted.
- Expense categories are limited to the predefined values in the `Category` enum. Requests containing unsupported categories are rejected during validation.
- Expense amounts are expected to be positive values greater than zero.
- Clients are expected to send dates in ISO-8601 format (`yyyy-MM-dd`).
- Expense IDs are generated automatically by the application and are unique for the lifetime of a running application instance.
- The API is intended for a single application instance and does not synchronize data across multiple running instances.

## Future Improvements

The current implementation provides a solid foundation that can be extended in several ways:

- **Database Integration** – Replace the in-memory repository with a persistent database such as PostgreSQL or MySQL while reusing the existing repository abstraction.
- **Authentication & Multi-User Support** – Add user authentication and authorization (e.g., Spring Security with JWT) so each user can securely manage their own expenses.
- **Caching** – Introduce a caching layer (e.g., Redis) for frequently accessed data such as expense summaries and category totals to improve performance.
- **Pagination & Sorting** – Support pagination, sorting, and more advanced filtering for efficient retrieval of large expense datasets.
- **API Documentation** – Generate interactive API documentation using OpenAPI/Swagger to simplify API exploration and client integration.
- **Persistent Audit & Logging** – Record application events and maintain audit logs for better monitoring, debugging, and traceability.
