Tizori Wallet — Backend 💳
A production-grade Spring Boot digital wallet backend with JWT authentication, Redis caching, Apache Kafka event streaming, Docker containerization, and GitHub Actions CI/CD.

Note: This project is built for learning, portfolio, and demonstration purposes.


Table of contents

Features
Tech stack
Project structure
System architecture
Prerequisites
Run with Docker
API reference
Swagger documentation
Testing
CI/CD pipeline
Security
Future enhancements


Features
AreaCapabilitiesAuthenticationUser registration and login. BCrypt password hashing. JWT token issuance and validation. Role-based access control (RBAC).WalletCreate wallet per user. Check balance. Add money. Transfer money between wallets. Redis-backed wallet cache for fast reads.TransactionsFull transaction history per user. Transaction summary with totals. Kafka event publishing on every wallet operation.AdminView all users. View all transactions. Dashboard analytics with platform-wide metrics.InfrastructureFully Dockerized with Docker Compose. GitHub Actions pipeline for build, test, and Docker Hub push. Swagger / OpenAPI documentation.

Tech stack

Backend: Java 21, Spring Boot 3.5, Spring Security, Spring Data JPA
Authentication: JWT (jjwt 0.12), BCrypt
Database: MySQL 8
Cache: Redis
Messaging: Apache Kafka
API docs: Swagger / OpenAPI (springdoc)
Build: Maven
Containerization: Docker, Docker Compose
CI/CD: GitHub Actions
Testing: JUnit 5, Mockito


Project structure
textcom.ewallet
├── admin          # Admin controllers and services
├── config         # Security, JWT, Redis, Kafka, CORS config
├── dto            # Request and response DTOs
├── transaction    # Transaction entity, repository, service, controller
├── user           # User entity, repository, service, controller
└── wallet         # Wallet entity, repository, service, controller

System architecture
text                    ┌─────────────────┐
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
                 │ Docker Build/Push  │
                 └────────────────────┘

Prerequisites

Java 21+
Maven 3.9+
Docker and Docker Compose


Run with Docker
Clone the repository and start all services with a single command:
bashgit clone https://github.com/ShivamAgrawal1909/Ewallet.git
cd Ewallet
docker-compose up --build
Services started:
ServicePortSpring Boot API8080MySQL3306Redis6379Kafka9092

API reference
User
MethodEndpointDescriptionPOST/api/users/registerRegister a new userPOST/api/users/loginLogin and receive JWT tokenGET/api/users/{id}Get user by ID
Wallet
MethodEndpointDescriptionPOST/api/wallet/create/{userId}Create wallet for userGET/api/wallet/balance/{userId}Get wallet balancePOST/api/wallet/add/{userId}Add money to walletPOST/api/wallet/transferTransfer money between wallets
Transactions
MethodEndpointDescriptionGET/api/transactions/user/{userId}Transaction historyGET/api/transactions/summary/{userId}Transaction summaryGET/api/transactions/allAll transactions (admin)
Admin
MethodEndpointDescriptionGET/api/admin/usersAll usersGET/api/admin/transactionsAll transactionsGET/api/admin/dashboardPlatform metrics

Swagger documentation
Once the application is running, open:
http://localhost:8080/swagger-ui/index.html
All endpoints are documented with request/response schemas.

Testing
bashmvn clean test
Test coverage includes:

JUnit 5 unit tests
Mockito mocks for service layer
UserService and UserController tests


CI/CD pipeline
GitHub Actions automatically triggers on every push:

Builds the project with Maven
Runs all tests
Builds the Docker image
Pushes the image to Docker Hub (shivamagr/ewallet-app)


Security

Passwords hashed with BCrypt
Stateless JWT authentication on every protected request
RBAC — admin and user roles enforced at the controller level
Secrets managed via environment variables — never hardcoded


 Future enhancements

- Email notifications on transactions
- Refresh token support
- Monitoring and logging (Actuator, ELK)
- Rate limiting
- Pagination for transaction history
- UPI payment integration
- Bank account linking and withdrawals
- Net banking support
- Transaction receipts via email/SMS


Author
Shivam Agrawal

GitHub: ShivamAgrawal1909

License
This project is created for learning, portfolio, and demonstration purposes.

Licensed under the MIT License.
