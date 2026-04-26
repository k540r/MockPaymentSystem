# MockPaymentSystem 💳

A Secure Mock Payment System built using **Spring Boot, JWT Authentication, and PostgreSQL**.  
This project simulates real-world fintech backend operations like wallet management, transactions, authentication, and admin control.

---

## 🚀 Features

- 🔐 JWT Authentication (Login / Register)
- 🔄 Refresh Token & Logout System
- 💰 Wallet Management (Balance, Withdraw, Transfer)
- 📊 Transaction Processing (Deposit / Withdraw / History)
- 👨‍💼 Admin Controls (Activate / Deactivate Users)
- 🚫 Blacklisted Token System
- 🧹 Scheduler for Expired Token Cleanup
- 📄 Role-Based Access (ADMIN / USER)

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- PostgreSQL
- Maven

---

## 📡 API Documentation

Swagger UI is integrated for testing APIs.

👉 Run project and open:

http://localhost:8080/swagger-ui/index.html


## ⚙️ Setup & Run

### 1. Clone the repository

```bash
git clone https://github.com/your-username/MockPaymentSystem.git
```
### 2. Configure PostgreSQL in application.properties

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```
### 3.Run the project
```bash
mvn spring-boot:run
```
## 🧠 Architecture
- Controller → Service → Repository → Database
- JWT Filter → Security Layer → Protected APIs
- Scheduler → Background Token Cleanup Job
## 🔒 Security Features
- JWT Authentication with Access & Refresh Tokens
- Logout with Token Blacklisting
- Role-Based Access Control (ADMIN / USER)
- Protected REST APIs
- Secure password encoding
- Token Expiry + Cleanup Scheduler
## 📌 API Examples
### 🔐 Login
```http
POST /api/auth/login
```
### 💰 Wallet Balance
```http
GET /api/wallet/balance?username=admin
```
### 👨‍💼 Admin Actions
```http
DELETE /api/admin/delete?username=user1
POST /api/admin/activate?username=user1
```

## 📈 Future Improvements
- Payment gateway integration (Razorpay/Stripe)
- Microservices architecture
- Docker deployment
- Email notifications
- Frontend dashboard (React/Angular)
## 👨‍💻 Author
Built as a backend fintech simulation project using Spring Boot, JWT, and PostgreSQL.
