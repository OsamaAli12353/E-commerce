# 🛒 E-Commerce Project

## Overview
Backend e-commerce application built with **Spring Boot**.  
Manages users, products, roles, and transactions, providing APIs for user management and purchasing operations.

## 🧩 Technologies Used
- Java 25  
- Spring Boot  
- Spring Data JPA  
- Hibernate  
- MySQL  
- Maven  

## 📁 Project Structure
src/main/java/ecommerce/ecommerce
│
├── DTO/                 # Data Transfer Objects
├── entity/              # Database entities
├── repository/          # JPA repositories
├── service/             # Service layer
└── ECommerceApplication.java

## ⚙️ Features
- User registration and role management  
- Product listing and management  
- Buying products with transaction tracking  
- DTO layer for clean API communication  

## 🔐 Security (Upcoming)
Planned integration of:
- **OTP verification** for secure login or checkout  
- **Authentication** using Spring Security  
- **Authorization** with role-based access control (Admin/User)  
- Future support for **JWT tokens**

## 🚀 How to Run
1. Clone the repository:
   git clone https://github.com/OsamaAli12353/E-commerce.git
2. Open in IntelliJ IDEA.  
3. Configure database in application.properties.  
4. Run:
   mvn spring-boot:run
5. Access the API:
   http://localhost:8080

## 📦 Example Endpoints
Method | Endpoint | Description
-------|-----------|-------------
POST | /buy | Buy a product
GET | /products | Retrieve all products
GET | /users/{id} | Retrieve user details

## 🧠 Author
Osama Ali
