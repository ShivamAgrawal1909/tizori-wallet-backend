# E-Wallet Platform 💳

A production-style Digital Wallet Backend built using Spring Boot, JWT Authentication, Redis Caching, Apache Kafka, Docker, and GitHub Actions CI/CD.

## 🚀 Tech Stack

* Java 21
* Spring Boot 3.5
* Spring Security
* JWT Authentication
* Spring Data JPA
* MySQL 8
* Redis
* Apache Kafka
* Swagger / OpenAPI
* Docker & Docker Compose
* GitHub Actions CI/CD
* Maven
* JUnit 5 & Mockito

---

## ✨ Features

### Authentication & Security

* User Registration
* User Login
* BCrypt Password Encryption
* JWT Authentication
* Role-Based Access Control (RBAC)
* Protected APIs

### Wallet Management

* Create Wallet
* Check Balance
* Add Money
* Transfer Money
* Redis Wallet Cache

### Transactions

* Transaction History
* Transaction Summary
* Kafka Event Publishing

### Admin Module

* View All Users
* View All Transactions
* Dashboard Analytics

### Infrastructure

* Dockerized Application
* Redis Integration
* Kafka Integration
* GitHub Actions CI/CD Pipeline
* Swagger API Documentation

---

## 📂 Project Structure

com.ewallet

├── admin

├── config

├── redis

├── transaction

├── user

├── wallet

└── dto

---
## 🏗️ System Architecture

```text
                    ┌─────────────────┐
                    │     Client      │
                    │ Swagger / App   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Spring Boot API │
                    │ JWT + Security  │
                    └───────┬─────────┘
                            │
         ┌──────────────────┼──────────────────┐
         │                  │                  │
         ▼                  ▼                  ▼
 ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
 │    MySQL     │   │    Redis     │   │    Kafka     │
 │ Persistence  │   │ Wallet Cache │   │ Event Queue  │
 └──────────────┘   └──────────────┘   └──────────────┘

                            │
                            ▼
                 ┌────────────────────┐
                 │ GitHub Actions CI  │
                 │ Docker Build/Test  │
                 └────────────────────┘
```


## 📡 API Modules

### User APIs

* POST /api/users/register
* POST /api/users/login
* GET /api/users/{id}

### Wallet APIs

* POST /api/wallet/create/{userId}
* GET /api/wallet/balance/{userId}
* POST /api/wallet/add/{userId}
* POST /api/wallet/transfer

### Transaction APIs

* GET /api/transactions/user/{userId}
* GET /api/transactions/summary/{userId}
* GET /api/transactions/all

### Admin APIs

* GET /api/admin/users
* GET /api/admin/transactions
* GET /api/admin/dashboard

---

## 🐳 Run with Docker

```bash
docker-compose up --build
```

Services:

* Spring Boot → 8080
* MySQL → 3306
* Redis → 6379
* Kafka → 9092

---

## 📖 Swagger Documentation

Open:

http://localhost:8080/swagger-ui/index.html

---

## 🧪 Testing

```bash
mvn clean test
```

Includes:

* JUnit 5
* Mockito
* Service Tests
* Controller Tests

---

## 🔄 CI/CD

GitHub Actions pipeline automatically:

* Builds the project
* Runs tests
* Builds Docker image
* Pushes Docker image to Docker Hub

---

## 🔒 Security

* BCrypt Password Hashing
* JWT Authentication
* RBAC Authorization
* Environment-based Secret Management

---

## 🚧 Future Enhancements

* Razorpay Integration
* Email Notifications
* Refresh Tokens
* Monitoring & Logging

---

## 👨‍💻 Author

Shivam Agrawal
