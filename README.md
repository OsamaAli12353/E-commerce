# 🛒 E-commerce Application

A robust, full-featured E-commerce platform built with Spring Boot, featuring **OTP verification with Redis**, JWT authentication, comprehensive product management, and secure transaction tracking.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-Cache-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

---

## 📋 Table of Contents

- [Features](#-features)
- [Technologies](#-technologies)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [OTP Verification System](#-otp-verification-system)
- [Security](#-security)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

- 🔐 **Secure Authentication** - JWT-based authentication system
- 📧 **OTP Verification** - Redis-powered OTP generation and verification for enhanced security
- 👥 **User Management** - Complete CRUD operations for users
- 📦 **Product Catalog** - Comprehensive product management
- 💳 **Transaction Processing** - Secure purchase and transaction tracking
- 🔒 **Role-Based Access Control** - ADMIN and CUSTOMER roles
- ⚡ **Redis Caching** - High-performance OTP storage with automatic expiration
- 🗄️ **Flexible Database** - Support for H2 (development) and MySQL (production)
- 🚀 **RESTful APIs** - Clean and well-documented API endpoints

---

## 🛠 Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core programming language |
| Spring Boot | 3.x | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | Data persistence |
| Spring Data Redis | 3.x | Redis integration for OTP caching |
| Redis | 7.x | In-memory data store for OTP |
| JWT | - | Token-based authentication |
| H2 Database | - | Development database |
| MySQL | 8.x | Production database |
| Maven | 3.x | Build & dependency management |
| Lombok | - | Code generation |

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+** for dependency management
- **Redis Server 7.0+** for OTP verification
- **MySQL 8.0+** (optional, for production)
- **Git** for version control
- **Postman** or similar tool for API testing (optional)

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/OsamaAli12353/E-commerce.git
cd E-commerce
```

### 2. Install and Start Redis

**On Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server
```

**On macOS:**
```bash
brew install redis
brew services start redis
```

**On Windows:**
Download from [Redis Windows releases](https://github.com/microsoftarchive/redis/releases) or use WSL.

**Verify Redis Installation:**
```bash
redis-cli ping
# Should respond with: PONG
```

### 3. Configure the Database

Edit `src/main/resources/application.properties`:

**For H2 Database (Development):**
```properties
spring.datasource.url=jdbc:h2:mem:ecommerce
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

**For MySQL (Production):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 4. Configure Redis Settings

Add to `application.properties`:
```properties
# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.timeout=60000

# OTP Configuration
otp.expiration.minutes=5
otp.length=6
```

### 5. Configure JWT Settings

Add to `application.properties`:
```properties
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

### 6. Build the Project

```bash
mvn clean install
```

---

## ▶️ Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Java

```bash
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

The application will start on **`http://localhost:8080`**

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080
```

### 🔑 Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `POST` | `/auth/login` | Authenticate user and receive JWT token | Public |
| `POST` | `/api/users/register` | Register a new user account | Public |
| `POST` | `/auth/request-otp` | Request OTP for verification | Public |
| `POST` | `/auth/verify-otp` | Verify OTP code | Public |
| `POST` | `/auth/resend-otp` | Resend OTP code | Public |

**Login Request Body:**
```json
{
  "username": "user@example.com",
  "password": "SecurePass123!"
}
```

**Login Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "user@example.com",
  "role": "CUSTOMER"
}
```

---

## 📧 OTP Verification System

### Overview

The application implements a secure OTP (One-Time Password) verification system using Redis for high-performance caching and automatic expiration.

### How It Works

1. **OTP Generation** - System generates a 6-digit random OTP
2. **Redis Storage** - OTP is stored in Redis with automatic expiration (5 minutes)
3. **Verification** - User submits OTP for validation
4. **Auto-Cleanup** - Redis automatically removes expired OTPs

### OTP Endpoints

#### 📤 Request OTP

```http
POST /auth/request-otp
Content-Type: application/json

{
  "email": "user@example.com",
  "purpose": "REGISTRATION" // or "PASSWORD_RESET", "LOGIN"
}
```

**Response:**
```json
{
  "success": true,
  "message": "OTP sent successfully to user@example.com",
  "expiresIn": 300
}
```

#### ✅ Verify OTP

```http
POST /auth/verify-otp
Content-Type: application/json

{
  "email": "user@example.com",
  "otp": "123456",
  "purpose": "REGISTRATION"
}
```

**Success Response:**
```json
{
  "success": true,
  "message": "OTP verified successfully",
  "verified": true
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Invalid or expired OTP",
  "verified": false
}
```

#### 🔄 Resend OTP

```http
POST /auth/resend-otp
Content-Type: application/json

{
  "email": "user@example.com",
  "purpose": "REGISTRATION"
}
```

**Response:**
```json
{
  "success": true,
  "message": "New OTP sent successfully",
  "expiresIn": 300
}
```

### OTP Features

- ✅ **6-digit secure random OTPs**
- ⏱️ **5-minute automatic expiration**
- 🔄 **Unlimited resend capability**
- 💾 **Redis-backed for high performance**
- 🔐 **Purpose-based verification** (Registration, Login, Password Reset)
- 🚀 **Automatic cleanup of expired OTPs**

### Redis Key Structure

OTPs are stored in Redis with the following key pattern:
```
otp:{purpose}:{email} -> {otpCode}
TTL: 300 seconds (5 minutes)
```

Example:
```
otp:REGISTRATION:user@example.com -> "123456"
```

---

### 👤 User Management Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `GET` | `/api/users` | Retrieve all users | ADMIN |
| `GET` | `/api/users/{id}` | Get user by ID | ADMIN, Self |
| `PUT` | `/api/users/{id}` | Update user information | ADMIN, Self |
| `DELETE` | `/api/users/{id}` | Delete user account | ADMIN, Self |

**Register Request Body:**
```json
{
  "username": "newuser@example.com",
  "password": "SecurePass123!",
  "fullName": "John Doe",
  "role": "CUSTOMER"
}
```

---

### 📦 Product Management Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `GET` | `/api/products/all` | List all products | Public |
| `GET` | `/api/products/{id}` | Get product details | Public |
| `POST` | `/api/products/add` | Add new product | ADMIN |
| `PUT` | `/api/products/update/{id}` | Update product | ADMIN |
| `DELETE` | `/api/products/delete/{id}` | Delete product | ADMIN |
| `POST` | `/api/products/buy` | Purchase product | CUSTOMER, ADMIN |

**Add Product Request Body:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stock": 50,
  "category": "Electronics"
}
```

**Buy Product Request Body:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

---

### 💳 Transaction Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `GET` | `/api/transactions` | List all transactions | ADMIN |
| `GET` | `/api/transactions/my` | List user's transactions | CUSTOMER, ADMIN |
| `GET` | `/api/transactions/{id}` | Get transaction details | ADMIN, Owner |

---

## 🔒 Security

### Password Requirements

Passwords must meet the following criteria:
- ✅ Minimum **8 characters** long
- ✅ At least **one uppercase letter** (A-Z)
- ✅ At least **one lowercase letter** (a-z)
- ✅ At least **one number** (0-9)
- ✅ At least **one special character** (@, #, $, %, etc.)

**Example:** `SecurePass123!`

### OTP Security Features

- 🔐 **Cryptographically secure random generation**
- ⏱️ **Time-limited validity** (5 minutes)
- 🔄 **Single-use tokens** (automatically deleted after verification)
- 📧 **Email-based delivery** (prevents unauthorized access)
- 🎯 **Purpose-specific verification** (prevents OTP reuse across different actions)

### Authentication

All secured endpoints require a JWT token in the Authorization header:

```http
Authorization: Bearer <your-jwt-token>
```

### User Roles

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access to all endpoints |
| **CUSTOMER** | Access to products, own profile, and transactions |

---

## 🧪 Testing the API

### Using cURL

**Request OTP:**
```bash
curl -X POST http://localhost:8080/auth/request-otp \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","purpose":"REGISTRATION"}'
```

**Verify OTP:**
```bash
curl -X POST http://localhost:8080/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","otp":"123456","purpose":"REGISTRATION"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@example.com","password":"Admin123!"}'
```

**Get Products:**
```bash
curl -X GET http://localhost:8080/api/products/all
```

**Add Product (Admin):**
```bash
curl -X POST http://localhost:8080/api/products/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{"name":"Phone","price":599.99,"stock":100}'
```

---

## 📁 Project Structure

```
E-commerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ecommerce/ecommerce/
│   │   │       ├── controller/              # REST API Controllers
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── ProductsController.java
│   │   │       │   ├── TransactionController.java
│   │   │       │   └── UserController.java
│   │   │       │
│   │   │       ├── DTO/                     # Data Transfer Objects
│   │   │       │   ├── BuyRequestDTO.java
│   │   │       │   ├── OTPRequestDTO.java
│   │   │       │   ├── OTPVerifyDTO.java
│   │   │       │   ├── ProductsDTO.java
│   │   │       │   ├── RoleDTO.java
│   │   │       │   ├── TransactionDTO.java
│   │   │       │   ├── UserDTO.java
│   │   │       │   └── UserWithDetailsDTO.java
│   │   │       │
│   │   │       ├── entity/                  # JPA Entities
│   │   │       │   ├── Products.java
│   │   │       │   ├── Roles.java
│   │   │       │   ├── Transaction.java
│   │   │       │   └── User.java
│   │   │       │
│   │   │       ├── repository/              # Data Access Layer
│   │   │       │   ├── ProductsRepository.java
│   │   │       │   ├── RolesRepository.java
│   │   │       │   ├── TransactionRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       │
│   │   │       ├── security/                # Security Configuration
│   │   │       │   ├── CustomUserDetails.java
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   ├── JwtAuthFilter.java
│   │   │       │   ├── JwtUtil.java
│   │   │       │   └── SecurityConfig.java
│   │   │       │
│   │   │       ├── service/                 # Business Logic Layer
│   │   │       │   ├── OTPService.java
│   │   │       │   ├── OTPServiceImpl.java
│   │   │       │   ├── PasswordValidator.java
│   │   │       │   ├── ProductService.java
│   │   │       │   ├── ProductServiceImpl.java
│   │   │       │   ├── TransactionService.java
│   │   │       │   ├── TransactionServiceImpl.java
│   │   │       │   ├── UserService.java
│   │   │       │   └── UserServiceImpl.java
│   │   │       │
│   │   │       ├── config/                  # Configuration Classes
│   │   │       │   └── RedisConfig.java
│   │   │       │
│   │   │       └── ECommerceApplication.java  # Main Application Class
│   │   │
│   │   └── resources/
│   │       ├── static/                      # Static resources
│   │       ├── templates/                   # Template files
│   │       └── application.properties       # Application configuration
│   │
│   └── test/                                # Test files
│
├── target/                                  # Compiled files (generated)
├── .gitattributes                          # Git attributes
├── .gitignore                              # Git ignore rules
├── HELP.md                                 # Spring Boot help documentation
├── mvnw                                    # Maven wrapper (Unix)
├── mvnw.cmd                                # Maven wrapper (Windows)
├── pom.xml                                 # Maven configuration
└── README.md                               # Project documentation
```

---

## 🐛 Troubleshooting

### Redis Connection Issues

**Problem:** Application can't connect to Redis

**Solution:**
```bash
# Check if Redis is running
redis-cli ping

# Start Redis if not running
sudo systemctl start redis-server  # Linux
brew services start redis          # macOS
```

### OTP Not Expiring

**Problem:** OTPs remain in Redis after expiration time

**Solution:** Check Redis TTL settings in `application.properties`:
```properties
otp.expiration.minutes=5
```

### Redis Commands for Debugging

```bash
# Connect to Redis CLI
redis-cli

# View all OTP keys
KEYS otp:*

# Check OTP value
GET otp:REGISTRATION:user@example.com

# Check TTL (time to live)
TTL otp:REGISTRATION:user@example.com

# Manually delete OTP
DEL otp:REGISTRATION:user@example.com

# Clear all keys (use with caution)
FLUSHALL
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Osama Ali**
- GitHub: [@OsamaAli12353](https://github.com/OsamaAli12353)

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Redis team for the high-performance caching solution
- JWT.io for authentication standards
- The open-source community

---

## 📞 Support

If you encounter any issues or have questions:
- 🐛 [Report a bug](https://github.com/OsamaAli12353/E-commerce/issues)
- 💡 [Request a feature](https://github.com/OsamaAli12353/E-commerce/issues)
- 📧 Contact: osamaalsharqawy92@gmail.com

---

## 🚀 Future Enhancements

- [ ] Email service integration for OTP delivery
- [ ] SMS-based OTP verification
- [ ] Rate limiting for OTP requests
- [ ] Admin dashboard for monitoring OTP usage
- [ ] Multi-factor authentication (MFA)
- [ ] Payment gateway integration
- [ ] Order tracking system
- [ ] Product reviews and ratings

---

**⭐ If you find this project useful, please consider giving it a star!**
