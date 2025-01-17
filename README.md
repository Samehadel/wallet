# Wallet Application

## Introduction
This project is a **Wallet Application** designed to provide users with the ability to link their accounts, track their spending, and gain valuable insights into their finances. The application is in the early stages of development and aims to empower users to:

- Set spending limits to control their expenses.
- Generate detailed financial reports to analyze spending habits.
- Manage multiple accounts seamlessly in one place.

The application follows a **microservices architecture** to ensure scalability, maintainability, and flexibility.

## Technologies Used
The project leverages modern technologies and frameworks to build a robust and efficient system. Below is a list of the main technologies used:

### Backend
- **Spring Boot**: For building RESTful APIs.
- **Spring Cloud Netflix Eureka**: For service discovery and registry.
- **Spring Cloud OpenFeign**: For inter-service communication.
- **Spring Boot Starter Data JPA**: For database interaction using JPA.
- **Spring Boot Starter Validation**: For validating user inputs.
- **Spring Kafka**: For message-based communication.
- **Resilience4j Circuit Breaker**: For fault tolerance.
- **Redisson**: For distributed caching.
- **Spring Retry**: For handling retry logic in case of failures.
- **Spring Boot Starter Log4j2**: For logging.

### API Documentation
- **Springdoc OpenAPI UI**: For generating API documentation.

### Utility Libraries
- **MapStruct**: For mapping DTOs and entities.
- **Lombok**: For reducing boilerplate code.
- **JJWT (Java JSON Web Token)**: For token-based authentication.
- **Jackson Databind & JSR310**: For JSON serialization and Java 8 date-time support.

### Testing
- **Spring Boot Starter Test**: For unit and integration testing.

### Database
- **SQL Server**: For storing application data.

### Deployment
- **Docker & Kubernetes (Minikube)**: For containerization and orchestration during local development.

## High-Level Design

### Microservices Architecture
The application consists of several microservices, each responsible for a specific functionality. These include:
1. **User Service**: Manages user profiles and authentication.
2. **Wallet Service**: Handles wallet operations such as balance tracking, spending limits, and transactions.
3. **Report Service**: Generates financial insights and reports for users.

### Service Discovery and Communication
- **Eureka Server** is used for service registration and discovery.
- **Feign Clients** enable seamless communication between microservices.

### API Gateway
The application uses an **API Gateway** (configured with Zuul routes) to manage and route requests to the appropriate microservices.

### Circuit Breaker and Fault Tolerance
- **Resilience4j** provides circuit breaker patterns to ensure fault tolerance and resilience in inter-service communication.

### Security
- The application leverages **JWT-based authentication** to secure APIs and ensure only authorized users can access their data.

### Caching and Messaging
- **Redisson** is used for caching frequently accessed data.
- **Kafka** is used for asynchronous messaging between microservices, ensuring scalability and eventual consistency.

## Future Enhancements
As the project progresses, additional features and optimizations will be implemented, such as:
- **Mobile Application Integration** for seamless access on the go.
- **AI-Powered Financial Insights** to provide personalized recommendations.
- **Multi-Currency Support** for international users.
- **Enhanced Security Features**, such as multi-factor authentication.

---
