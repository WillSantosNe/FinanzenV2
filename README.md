# 🚀 Finanzen API | Enterprise Financial Core

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg)
![Hexagonal Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blue.svg)
![Status](https://img.shields.io/badge/Status-Under%20Construction-yellow.svg)

> **Finanzen** is an enterprise-grade RESTful API designed to manage financial transactions with extreme reliability, scalability, and security. Built from the ground up to handle high throughput and complex business rules, this system serves as a technical portfolio demonstrating advanced software engineering practices.

## 🎯 The Vision
This is not a simple CRUD application. The Finanzen API is built mirroring the architectural standards of major financial institutions. It completely isolates the core business logic from external frameworks, ensuring a system that is testable, agnostic, and ready for the cloud.

## 🛠️ Tech Stack & Engineering Practices

- **Core & Architecture:** Java 21, Spring Boot 3, Hexagonal Architecture (Ports and Adapters).
- **Security:** Spring Security 3.x, Stateless JWT Authentication, Role-based Access Control (RBAC), BCrypt.
- **Polyglot Persistence:** - PostgreSQL (Primary Relational Database for Transactions & Outbox events).
  - MongoDB (NoSQL for high-speed Audit Logging via Spring AOP).
  - Redis (In-memory cache for high-performance querying and Rate Limiting).
- **Event-Driven & Resilience:** Apache Kafka (KRaft), Outbox Pattern, OpenFeign, Resilience4j (Circuit Breakers).
- **Testing Mastery:** JUnit 5, Mockito, Testcontainers (Postgres, Redis, Kafka), ArchUnit (Architecture Testing), JaCoCo (Coverage).
- **DevOps & Cloud:** Docker (Multi-stage builds), GitHub Actions (CI/CD), Prometheus/Grafana (Observability), Microsoft Azure.

---

## 🗺️ The Engineering Roadmap (8-Week Journey)

This project is being systematically developed through a rigorous 2-month engineering roadmap.

### 🛡️ Month 1: Core Engine, Security & Polyglot Data
- [x] **Week 1: True Hexagonal Architecture:** Isolation of the pure Domain model, implementation of Inbound/Outbound Ports, Web and Persistence Adapters, and explicit Mappers.
- [ ] **Week 2: Security Foundation:** Implementation of User domain, Spring Security Filter Chains, and robust BCrypt encoding.
- [ ] **Week 3: Stateless Authentication (JWT Engine):** JWT token generation, OncePerRequestFilter, Security Context Holder manipulation, and custom Exception Handling for 401/403.
- [ ] **Week 4: NoSQL & High Performance:** Audit logging in MongoDB via AOP, Caching with Redis (`@Cacheable`/`@CacheEvict`), and API Rate Limiting.

### 🚀 Month 2: Resilience, Kafka Events & Cloud Native
- [ ] **Week 5: External Integrations & Outbox Pattern:** Feign Clients with Resilience4j Fallbacks, Scheduled Tasks, and transactional Outbox event recording.
- [ ] **Week 6: Event-Driven Architecture (Apache Kafka):** Kafka infrastructure via Docker, Producers/Consumers, Retry Policies, and Dead Letter Queues (DLQ).
- [ ] **Week 7: Senior-Level Testing:** Architecture validation with ArchUnit, isolated environment testing with Testcontainers, MockMvc, and coverage reports.
- [ ] **Week 8: DevOps & Cloud Deployment:** Actuator metrics exposed to Prometheus/Grafana, Dockerization, GitHub Actions CI/CD pipeline, and final deployment to Azure App Service and Azure DB.

---

## ⚙️ How to Run (Local Environment)

*(This section will be continually updated as the DevOps infrastructure is built in Week 8).*

Currently, to run the application in its Hexagonal state:
1. Ensure you have Java 21+ installed.
2. Clone the repository: `git clone https://github.com/your-username/finanzen-api.git`
3. Configure your local PostgreSQL database in `application.properties`.
4. Run via Maven: `./mvnw spring-boot:run`

---
*Built with passion and architectural rigor.*
