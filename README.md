# E-Wallet Platform 💳

A production-grade E-Wallet platform built with Spring Boot Microservices, Kafka, Docker, and CI/CD pipeline.

## 🚀 Tech Stack

- **Backend:** Java 25, Spring Boot 3.5.14
- **Database:** MySQL 8.0
- **Messaging:** Apache Kafka
- **Security:** Spring Security + BCrypt
- **Containerization:** Docker + Docker Compose
- **CI/CD:** GitHub Actions + Docker Hub
- **Build Tool:** Maven

## 📋 Features

- ✅ User Registration & Login with BCrypt encryption
- ✅ Wallet Creation & Management
- ✅ Add Money to Wallet
- ✅ Transfer Money between users
- ✅ Transaction History
- ✅ Async notifications via Kafka
- ✅ Dockerized application
- ✅ Automated CI/CD pipeline

## 🏗️ Project Structure
com.ewallet
├── user
│   ├── User.java
│   ├── UserRepository.java
│   ├── UserService.java
│   └── UserController.java
├── wallet
│   ├── Wallet.java
│   ├── WalletRepository.java
│   ├── WalletService.java
│   └── WalletController.java
├── transaction
│   ├── Transaction.java
│   ├── TransactionRepository.java
│   ├── TransactionService.java
│   └── TransactionController.java
└── config
├── SecurityConfig.java
├── KafkaProducer.java
└── KafkaConsumer.java
## 📡 API Endpoints

### User APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/users/register | Register new user |
| POST | /api/users/login | Login user |
| GET | /api/users/{id} | Get user by ID |

### Wallet APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/wallet/create/{userId} | Create wallet |
| GET | /api/wallet/balance/{userId} | Get balance |
| POST | /api/wallet/add/{userId} | Add money |
| POST | /api/wallet/transfer | Transfer money |

### Transaction APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/transactions/save | Save transaction |
| GET | /api/transactions/history/{userId} | Get history |

## 🐳 Run with Docker

```bash
docker-compose up
```

## ⚙️ Setup Locally

```bash
# Clone the repo
git clone https://github.com/ShivamAgrawal1909/Ewallet.git

# Run the application
cd Ewallet
mvn spring-boot:run
```

## 👨‍💻 Author
Shivam Agrawal