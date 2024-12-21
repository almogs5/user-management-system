# User Management System - Spring Boot Application

This project is a backend system for managing and viewing user information and statistics. It includes user management APIs, general program statistics, error handling, logging, caching, and PostgreSQL integration.

The system is built using **Java Spring Boot**, **Maven**, **JPA with Hibernate ORM**, and a **PostgreSQL** database.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
3. [Building the Application](#building-the-application)
4. [Running the Application Locally](#running-the-application-locally)
5. [Configuring PostgreSQL Database](#configuring-postgresql-database)
6. [API Endpoints](#api-endpoints)
7. [Error Handling and Logging](#error-handling-and-logging)
8. [Caching](#caching)
19. [Health Check](#health-check)
---

## 1. Prerequisites

Before you can build and run the application locally, you will need the following tools and software installed:

- **Java 17 or higher** (LTS version recommended)
- **Maven** (for managing dependencies and building the project)
- **PostgreSQL** (for the database)
- **IDE** (e.g., IntelliJ IDEA, Eclipse, Visual Studio Code, etc.) or command-line interface

Additionally, ensure that the PostgreSQL service is running on your machine or in a cloud environment.

---

## 2. Project Setup

### Clone the Repository

Clone the repository to your local machine:

```bash
git https://github.com/almogs5/user-management-system.git
cd user-management-system
```

### Maven Dependencies

The project uses Maven for dependency management. When you build the project (either via your IDE or command line), Maven will automatically download the necessary dependencies specified in the `pom.xml` file.

---

## 3. Building the Application

### 1. Build using Maven

To build the project using Maven, open a terminal in the project directory and run:

```bash
mvn clean install
```

This will compile the project, run tests, and package the application into a `.jar` file under the `target/` directory.

### 2. Build using IDE

Alternatively, you can open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse, or Visual Studio Code) and build the project directly. The IDE will automatically fetch the dependencies specified in the `pom.xml` file.

---

## 4. Running the Application Locally

Once you have built the project, you can run the application locally by following these steps:

### 1. Run with Maven

To run the application directly with Maven, use the following command:

```bash
mvn spring-boot:run
```

This will start the Spring Boot application on the default port `8080`. 

### 2. Run the `.jar` File

If you want to run the packaged `.jar` file:

```bash
java -jar target/user-management-system-0.0.1-SNAPSHOT.jar
```

---

## 5. Configuring PostgreSQL Database

To use PostgreSQL as the database, ensure that PostgreSQL is installed and running. You also need to configure the connection settings in the `application.yaml` file.

### 1. Install PostgreSQL (If not already installed)

You can follow the installation instructions from the official PostgreSQL documentation:
- [PostgreSQL Downloads](https://www.postgresql.org/download/)

### 2. Configure `application.yaml`

In the `src/main/resources/application.yaml` file, update the database connection settings to match your PostgreSQL installation:

```properties
# PostgreSQL Database connection settings
spring.datasource.url=jdbc:postgresql://localhost:5432/user_db  # Replace with your database URL
spring.datasource.username=postgres  # Your PostgreSQL username
spring.datasource.password=yourpassword  # Your PostgreSQL password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate settings
spring.jpa.hibernate.ddl-auto=update  # Use 'update' in development, 'validate' or 'none' in production
spring.jpa.show-sql=true  # Show SQL queries for debugging
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect  # Use PostgreSQL dialect
```
---

## 6. API Endpoints

Here is a list of the main API endpoints in the system:

### 1. **User Management APIs**

- **Login (POST)**  
  Endpoint: `/api/v1/users/login`  
  Description: Authenticates a user based on email and password.

- **Sign In (POST)**  
  Endpoint: `/api/v1/users/signun`  
  Description: Registers a new user.

- **Get All Users (GET)**  
  Endpoint: `/api/v1/users`  
  Description: Retrieves a list of all users.

- **Update Status (PUT)**  
  Endpoint: `/api/v1/users/{userId}/status`  
  Description: Updates the status of a user.

### 2. **Statistics API**

- **Get General Statistics (GET)**  
  Endpoint: `/api/v1/users/stats`  
  Description: Returns general statistics about the application:
    - Total Users
    - Total Requests
    - Average Request Handling Time

---

## 7. Error Handling and Logging

The application provides robust error handling and logs any issues that occur. Errors are handled globally through the `@ControllerAdvice` class.

### Example Error Responses

- **Database connection error**: If there’s an issue with connecting to the database, a clear error message will be returned such as:
  ```json
  {
    "error": "Database connection failure. Please check if the database server is running."
  }
  ```

- **Invalid input error**: When invalid input is detected, the application will return a 400 error:
  ```json
  {
    "error": "Invalid request parameters."
  }
  ```

All errors are logged with an appropriate log level (`ERROR` for severe issues) in the log file.

---

## 8. Caching

The application includes caching to improve performance for frequently accessed data. The default cache implementation is `simple` caching, and the cache name used is `users`.

Caching helps to minimize database calls for frequently queried data.

---
## 9. Health Check

The application includes Spring Boot Actuator to expose a health check endpoint for monitoring purposes.

### Health Check Endpoint

You can access the health status by visiting the following URL:

```
http://localhost:8080/actuator/health
```

This endpoint will return a status of the application, including database connectivity.

Example Response:

```json
{
  "status": "UP"
}
```

If the application can't connect to the database or experiences other issues, the status will be `DOWN`.

---
## Conclusion

This Spring Boot-based User Management System provides APIs for managing users, handling user statistics, and managing caching. It is connected to a PostgreSQL database and includes robust error handling and logging features for seamless operation.

Feel free to clone the repository, set it up locally, and start using the APIs to manage users and view system statistics.

---
