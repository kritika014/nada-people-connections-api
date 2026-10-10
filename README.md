# People & Connections API — Nada Assignment

A lightweight, in-memory REST API built with Core Java 17 and Javalin to manage people, direct contact connections, and find shortest social paths up to 3 hops.

---

## 1. How to Run the Server and Tests

### Prerequisites
* Java JDK 17 or newer installed.
* Apache Maven 3.8+ installed.

### Running the Server
1. Clone the repository:
   ```bash
   git clone [https://github.com/kritika014/nada-people-connections-api.git](https://github.com/kritika014/nada-people-connections-api.git)
   cd nada-people-connections-api

   Build and start the server using Maven:
   mvn clean compile exec:java -Dexec.mainClass="com.assignment.App"

   The server will start and listen on port 8080 (http://localhost:8080).

   To execute the automated JUnit 5 test suite:
   mvn clean test


2. What I Would Improve With More Time
   Advanced Error Handling & Validation: Implement global validation filters for stricter schema sanitization.

  Authentication & Rate Limiting: Add API key/token authentication and rate-limiting rules.


3.AI Tools Used
Gemini: Used as an AI coding collaborator for project scaffolding, Javalin configuration guidance, Jackson JSON setup, and designing the BFS path search algorithm.
