
# Inventory Management System

A microservice built with Spring Boot and PostgreSQL for managing products and categories, featuring role-based access control using JWT authentication.

## Prerequisites

- Docker & Docker Compose
- Java 11+
- Maven

## Getting Started

1. **Clone the Repository**

   ```bash
   git clone <repository-url>
   cd inventory-management-system
   ```

2. **Build the Project**

   ```bash
   mvn clean install
   ```

3. **Run with Docker Compose**

   ```bash
   docker-compose up
   ```

   The PostgreSQL database schema and sample data are managed with Flyway migrations.

4. **Access Swagger UI**

    - URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Users & Roles

- **Admin (admin/admin)**: Full access to products and categories.
- **User (user/password)**: Access to products only.

## Authentication

1. **Obtain JWT Token**: Use `/api/auth/login` endpoint.
2. **Use Token**: Add `Authorization: Bearer <token>` header for requests.

## Technologies Used

- Java 11+, Spring Boot, Spring Data JPA
- PostgreSQL, Docker, Flyway
- JWT Authentication, Swagger
