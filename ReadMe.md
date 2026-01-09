# 📚 Library Management System – Spring Boot REST API

A **secure, role-based Library Management System** built using **Spring Boot**, **JWT authentication**, and **MySQL**.
This application supports **Admin & User roles**, book management, and book issue/return workflows.

---

## 🚀 Features

### 🔐 Authentication & Authorization

* JWT-based authentication
* Role-based access control (ADMIN / USER)
* Secure password hashing using BCrypt
* Stateless REST API

### 👤 User Management

* Register normal users
* Register admin users (ADMIN-only access)
* Login and receive JWT token

### 📚 Book Management (ADMIN)

* Add new books
* Update book details
* Delete books
* View all books
* View book by ID

### 🔄 Issue & Return System

* Users can issue available books
* Automatic due date calculation (14 days)
* Book availability & quantity tracking
* Return issued books

---

## 🛠 Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security**
* **JWT (JSON Web Token)**
* **Spring Data JPA**
* **MySQL**
* **Lombok**
* **Hibernate**
* **Maven**

---

## 📂 Project Structure

```
com.example.LibraryManagement
│
├── controller
├── dto
├── entity
├── jwt
├── repository
├── security
├── service
└── LibraryManagementApplication.java
```

---

## 🔐 Security Design

* JWT authentication filter (`OncePerRequestFilter`)
* Role-based endpoint protection using `@PreAuthorize`
* Stateless session management
* Secure token validation on every request

---

## ⚙️ Environment Configuration

This project uses **environment variables** for sensitive data.

### `application.yml` (GitHub Safe)

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/library}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secretkey: ${JWT_SECRET:change_this_secret}
  expiration: ${JWT_EXPIRATION:3600000}
```

---

## 🔑 Required Environment Variables

| Variable       | Description                |
| -------------- | -------------------------- |
| DB_URL         | MySQL database URL         |
| DB_USERNAME    | Database username          |
| DB_PASSWORD    | Database password          |
| JWT_SECRET     | Secret key for JWT signing |
| JWT_EXPIRATION | Token expiration time (ms) |

---

## ▶️ Run Locally

### 1️⃣ Set Environment Variables

#### Linux / Mac

```bash
export DB_URL=jdbc:mysql://localhost:3306/library
export DB_USERNAME=root
export DB_PASSWORD=your_password
export JWT_SECRET=your_secret_key
export JWT_EXPIRATION=3600000
```

#### Windows (PowerShell)

```powershell
setx DB_URL "jdbc:mysql://localhost:3306/library"
setx DB_USERNAME "root"
setx DB_PASSWORD "your_password"
setx JWT_SECRET "your_secret_key"
setx JWT_EXPIRATION "3600000"
```

---

### 2️⃣ Run Application

```bash
mvn spring-boot:run
```

Server starts on:

```
http://localhost:8080
```

---

## 📌 API Endpoints

### 🔐 Authentication

| Method | Endpoint                   | Access |
| ------ | -------------------------- | ------ |
| POST   | `/auth/registernormaluser` | Public |
| POST   | `/auth/login`              | Public |
| POST   | `/admin/registeradminuser` | ADMIN  |

---

### 📚 Books

| Method | Endpoint                   | Access        |
| ------ | -------------------------- | ------------- |
| GET    | `/books/getallbooks`       | Authenticated |
| GET    | `/books/getbooksbyid/{id}` | Authenticated |
| POST   | `/books/addbook`           | ADMIN         |
| PUT    | `/books/updatebook/{id}`   | ADMIN         |
| DELETE | `/books/delete/{id}`       | ADMIN         |

---

### 🔄 Issue / Return

| Method | Endpoint                                   |
| ------ | ------------------------------------------ |
| POST   | `/issuerecords/issuebook/{bookId}`         |
| POST   | `/issuerecords/returnbook/{issueRecordId}` |

---

## 🧪 API Testing

* Tested using **Postman**
* JWT token passed via:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 💡 Key Learnings

* Implemented JWT authentication from scratch
* Applied role-based security using Spring Security
* Designed RESTful APIs following best practices
* Used environment variables for secure configuration
* Built real-world business logic (issue/return flow)

---

## 📈 Resume Worthy Highlights

✔ JWT Authentication
✔ Spring Security
✔ Role-based Authorization
✔ REST API Design
✔ MySQL + JPA
✔ Clean layered architecture

---

## 👨‍💻 Author

**Aachal Vaishnav**
Backend Developer | Java | Spring Boot

---