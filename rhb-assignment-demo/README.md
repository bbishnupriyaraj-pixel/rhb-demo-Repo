# Spring Boot JDK 17 Demo

Layered REST API demonstrating HTTP methods, paginated search, HTTP/AOP logging, H2 in-memory persistence, an employee-to-department join, an outbound HTTP call, and unit tests.

## Requirements
- JDK 17
- Maven 3.8+
- Network access for the Google external API example

## Run
```bash
mvn clean test
mvn spring-boot:run
```
Base URL: `http://localhost:8080`  
H2 console: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:demodb`; username `sa`; blank password.

Seed data: Engineering and Operations departments with three employees. H2 is in-memory and resets when the app stops.

## Endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/employees?q=asha&page=0&size=10` | Search with pagination |
| GET | `/api/employees/{id}` | Get employee |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |
| GET | `/api/employees/by-department?name=Engineering` | Join query across employees and departments |
| GET | `/api/external/google-check` | Calls Google and returns URL plus response body length |

POST/PUT body:
```json
{
  "firstName": "Priya",
  "lastName": "Nair",
  "email": "priya.nair@example.com",
  "departmentId": 1
}
```
Seeded department IDs are `1` (Engineering) and `2` (Operations).

## Logging notes
`HttpLoggingFilter` logs HTTP method, URI, query string, response status, and duration. `LoggingAspect` logs controller arguments, result type, duration, and exceptions. The demo deliberately does not log full request/response bodies to avoid leaking personal data or credentials. Add redaction/correlation IDs and review privacy requirements before production use.

## Code layout
- `employee`: entity, request/response DTOs, repository, service and controller
- `department`: entity and repository
- `external`: outbound HTTP integration
- `logging`: servlet filter and Spring AOP
- `common`: centralized API exception handler

## Postman
Import `postman/SpringBoot-JDK17-Demo.postman_collection.json`. The collection includes all main endpoints. Change collection variables if needed.

## Publish to GitHub
I cannot publish to your GitHub account from this chat. To publish:
1. Create an empty repository on GitHub.
2. Extract this ZIP, open a terminal in the project folder, then run:
```bash
git init
git add .
git commit -m "Add Spring Boot JDK 17 demo"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```
Replace the remote URL with your own repository URL. Do not commit credentials or production secrets.
