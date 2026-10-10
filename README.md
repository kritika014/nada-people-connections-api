# People & Connections API — Nada Assignment

A lightweight, in-memory REST API built with **Core Java 17** and **Javalin** to manage people, direct contact connections, and find shortest social paths up to 3 hops.

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


  1. Build and start the server using Maven:
   mvn clean compile exec:java -Dexec.mainClass="com.assignment.App"

  2. The server will start and listen on port 8080 (http://localhost:8080).

  3. Running the Tests
To execute the automated JUnit 5 test suite (including validation for both sides of the 3-hop limit):mvn clean test


 What I Would Improve With More Time:
 
 Advanced Error Handling & Validation: Implement global validation filters for stricter schema sanitization.

Authentication & Rate Limiting: Add API key or token-based authentication and rate-limiting rules for secure production usage.

OpenAPI/Swagger Documentation: Export an interactive API documentation interface for easier client-side testing.



AI Tools Used

Gemini: Used as an AI coding collaborator for project scaffolding, Javalin configuration guidance, Jackson JSON mapping setup, and designing the graph Breadth-First Search (BFS) shortest path algorithm with the strict 3-hop limit.
   

   
