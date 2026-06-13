# 💰 Tizori Wallet — Backend

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Redis](https://img.shields.io/badge/Redis-Cache-red?style=flat-square&logo=redis)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-Events-black?style=flat-square&logo=apachekafka)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=flat-square&logo=docker)
![CI/CD](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-2088FF?style=flat-square&logo=githubactions)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

A production-grade **Spring Boot digital wallet backend** with JWT authentication, Redis caching, Apache Kafka event streaming, Docker containerization, and GitHub Actions CI/CD.

> ⚠️ This project is built for **learning, portfolio, and demonstration** purposes.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [System Architecture](#-system-architecture)
- [Prerequisites](#-prerequisites)
- [Run with Docker](#-run-with-docker)
- [API Reference](#-api-reference)
- [Default Credentials](#-default-credentials)
- [Security](#-security)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Swagger Documentation](#-swagger-documentation)
- [Testing](#-testing)
- [Future Enhancements](#-future-enhancements)

---

## ✨ Features

### 👤 Users

| Feature | Description |
|---|---|
| **Register** | Create a new account with email and password |
| **Login** | Sign in and receive a JWT access token |
| **BCrypt Hashing** | All passwords securely hashed before storage |
| **RBAC** | Role-based access — `USER` and `ADMIN` roles enforced at controller level |

### 💳 Wallet

| Feature | Description |
|---|---|
| **Create Wallet** | One wallet per user |
| **Check Balance** | Real-time balance with Redis cache for fast reads |
| **Add Money** | Top up wallet balance |
| **Transfer Money** | Send money between wallets with transaction tracking |

### 📊 Transactions

| Feature | Description |
|---|---|
| **Transaction History** | Full per-user history |
| **Transaction Summary** | Aggregated totals per user |
| **Kafka Events** | Every wallet operation publishes an event to Kafka |

### 🔧 Admin

| Feature | Description |
|---|---|
| **All Users** | View and manage all registered users |
| **All Transactions** | Platform-wide transaction view |
| **Dashboard** | Analytics and platform metrics |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5, Spring Security, Spring Data JPA |
| **Authentication** | JWT (jjwt 0.12), BCrypt |
| **Database** | MySQL 8 |
| **Cache** | Redis |
| **Messaging** | Apache Kafka |
| **API Docs** | Swagger / OpenAPI (springdoc) |
| **Build** | Maven |
| **Containerization** | Docker, Docker Compose |
| **CI/CD** | GitHub Actions |
| **Testing** | JUnit 5, Mockito |

---

## 📁 Project Structure

```
src/main/java/com/ewallet/
├── admin/              # Admin controllers and services
├── config/             # Security, JWT, Redis, Kafka, CORS configuration
├── dto/                # Request and response DTOs
├── transaction/        # Transaction entity, repository, service, controller
├── user/               # User entity, repository, service, controller
└── wallet/             # Wallet entity, repository, service, controller
```

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Client / Swagger UI                   │
└─────────────────────────┬───────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────┐
│              Spring Boot REST API Layer                  │
│                  JWT + Spring Security                   │
└──────────┬──────────────┬──────────────┬────────────────┘
           │              │              │
    ┌──────▼──────┐ ┌─────▼─────┐ ┌────▼────────┐
    │   MySQL 8   │ │   Redis   │ │    Kafka    │
    │ Persistence │ │  Wallet   │ │   Events    │
    │             │ │   Cache   │ │   Queue     │
    └─────────────┘ └───────────┘ └─────────────┘
           │
┌──────────▼──────────────────────────────────────────────┐
│           GitHub Actions CI  →  Docker Build/Push       │
└─────────────────────────────────────────────────────────┘
```

---

## ✅ Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose

---

## 🐳 Run with Docker

Clone the repository and start all services with a single command:

```bash
git clone https://github.com/ShivamAgrawal1909/Ewallet.git
cd Ewallet
docker-compose up --build
```

**Services started:**

| Service | Port |
|---|---|
| Spring Boot API | `8080` |
| MySQL | `3306` |
| Redis | `6379` |
| Kafka | `9092` |

---

## 📡 API Reference

### 👤 User Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Register a new user |
| `POST` | `/api/users/login` | Login and receive JWT token |
| `GET` | `/api/users/{id}` | Get user by ID |

### 💳 Wallet Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/wallet/create/{userId}` | Create wallet for user |
| `GET` | `/api/wallet/balance/{userId}` | Get wallet balance |
| `POST` | `/api/wallet/add/{userId}` | Add money to wallet |
| `POST` | `/api/wallet/transfer` | Transfer money between wallets |

### 📊 Transaction Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/transactions/user/{userId}` | Transaction history |
| `GET` | `/api/transactions/summary/{userId}` | Transaction summary |
| `GET` | `/api/transactions/all` | All transactions *(admin only)* |

### 🔧 Admin Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/admin/users` | All users |
| `GET` | `/api/admin/transactions` | All transactions |
| `GET` | `/api/admin/dashboard` | Platform metrics |

---

## 🔐 Default Credentials

| Role | Email | Password |
|---|---|---|
| Admin | `admin@tizori.com` | `admin123` |
| User | `user@tizori.com` | `user123` |

> ⚠️ Change credentials before deploying to any environment.

---

## 🔒 Security

- Passwords hashed with **BCrypt**
- **Stateless JWT** authentication on every protected request
- **RBAC** — `ADMIN` and `USER` roles enforced at controller level
- Secrets managed via **environment variables** — never hardcoded

---

## ⚙️ CI/CD Pipeline

GitHub Actions automatically triggers on every push:

```
Push to main
    │
    ├── 1. Build with Maven
    ├── 2. Run all tests (JUnit + Mockito)
    ├── 3. Build Docker image
    └── 4. Push to Docker Hub  →  shivamagr/ewallet-app
```

---

## 📖 Swagger Documentation

Once the application is running, open:

```
http://localhost:8080/swagger-ui/index.html
```

All endpoints are documented with request/response schemas.

---

## 🧪 Testing

```bash
./mvnw clean test
```

Test coverage includes:

- ✅ JUnit 5 unit tests
- ✅ Mockito mocks for service layer
- ✅ `UserService` and `UserController` tests

---

## 🚀 Future Enhancements

- [ ] Email notifications on transactions
- [ ] Refresh token support
- [ ] Monitoring and logging (Actuator, ELK)
- [ ] Rate limiting
- [ ] Pagination for transaction history
- [ ] UPI payment integration
- [ ] Bank account linking and withdrawals
- [ ] Net banking support
- [ ] Transaction receipts via email/SMS

---

## 👨‍💻 Author

**Shivam Agrawal**

[![GitHub](https://img.shields.io/badge/GitHub-ShivamAgrawal1909-181717?style=flat-square&logo=github)](https://github.com/ShivamAgrawal1909)

---

## 📄 License

This project is created for learning, portfolio, and demonstration purposes.

Licensed under the [MIT License](LICENSE).
