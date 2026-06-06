# AuthCore

A standalone JWT authentication microservice built with Spring Boot, demonstrating production-style auth patterns: dual-token strategy, Redis-backed token blacklist, BCrypt password hashing, and role-based access control.

## Problem Statement

Most tutorials bake authentication into the application they protect. Real systems isolate auth into a dedicated service so multiple applications can share a single source of truth for identity, and so the auth layer can be scaled, deployed, and secured independently. AuthCore is a minimal but realistic implementation of that pattern.

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.x, Spring Security 6
- **Auth:** JWT (HS256), BCrypt
- **Persistence:** MySQL 8 (user store), Redis 7 (token blacklist)
- **Build:** Maven
- **Containerization:** Docker Compose (MySQL + Redis)
- **Docs:** Swagger UI (springdoc-openapi)
- **Tests:** JUnit 5, Mockito

## Setup

> Detailed setup instructions will be added as the project is built.

Prerequisites: Java 17+, Maven 3.9+, Docker Desktop.

```bash
git clone https://github.com/Swayam020/authcore.git
cd authcore
docker compose up -d
./mvnw spring-boot:run
```

## Status

- [x] Milestone 0 - Repository initialized
- [x] Milestone 1 — Spring Boot project scaffolded
- [ ] Milestone 2 — Docker Compose (MySQL + Redis)
- [ ] Milestone 3 — User entity and repository
- [ ] Milestone 4 — Registration with BCrypt
- [ ] Milestone 5 — JWT service
- [ ] Milestone 6 — Login endpoint
- [ ] Milestone 7 — JWT authentication filter
- [ ] Milestone 8 — Spring Security configuration
- [ ] Milestone 9 — Token refresh
- [ ] Milestone 10 — Redis blacklist and logout
- [ ] Milestone 11 — Role-based access control
- [ ] Milestone 12 — Unit tests
- [ ] Milestone 13 — Swagger documentation
- [ ] Milestone 14 — v1.0.0 release

## License

MIT — see [LICENSE](./LICENSE).