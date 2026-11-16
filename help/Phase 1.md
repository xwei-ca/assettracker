# Phase 1 — Spring Boot REST API with JPA
## Step 0: Prerequisites
Make sure you have:

- **JDK 17** or higher installed (`java -version` to check, this example project was created on **JDK 17**)

- **Maven** installed (`mvn -version`) — or use the Maven wrapper generated with the project

- An IDE — IntelliJ IDEA (Community is fine) or VS Code with the Java extension pack (I used VS Code for this project)

- **Postman** or **curl** for testing

---

## Step 1: Create the project
The easiest way is **Spring Initializr** ([start.spring.io](start.spring.io)). Choose:

- **Project:** Maven

- **Language:** Java

- **Spring Boot:** 4.1.2 (the version maybe higher)

- **Group:** `com.example`

- **Artifact:** `assettracker`

- **Packaging:** Jar

- **Java:** 17 or higher (this project was created on 17)

**Dependencies to add:**

- Spring Web

- Spring Data JPA

- H2 Database (for development — we'll keep it simple)

- Spring Boot DevTools (optional, for hot reload)

- Validation (for input validation)

Download, unzip, and open in your IDE. Alternatively, generate it from the command line:

```
bash
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,h2,validation,devtools \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=4.1.2 \
  -d javaVersion=17 \
  -d groupId=com.example \
  -d artifactId=assettracker \
  -o assettracker.zip
unzip assettracker.zip -d assettracker
cd assettracker
```

---

## Step 2: Understand the structure
After generation, you'll have:

```
assettracker/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/assettracker/
    │   │   └── AssetTrackerApplication.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/example/assettracker/
            └── AssetTrackerApplicationTests.java
```

`AssetTrackerApplication.java` is the entry point — the `@SpringBootApplication` annotation starts the embedded Tomcat server and scans for components.

---

## Step 3: Configure the database
Open `src/main/resources/application.properties` and add:

```
# H2 in-memory database for development
spring.datasource.url=jdbc:h2:mem:assettracker
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 web console (optional, handy for inspecting data)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
**What this does:**

- `jdbc:h2:mem:assettracker` — an in-memory database; data disappears on restart, which is fine for learning.

- `ddl-auto=update` — Hibernate automatically creates/updates tables based on your entity classes.

- `show-sql=true` — prints the SQL Hibernate generates, so you can see what's happening.

- The H2 console lets you browse the data at `http://localhost:8080/h2-console`.

---

## Step 4: Create the entity
Create a new package `com.example.assettracker.model` and a class `Asset.java`.
**What this does:**

- `@Entity` tells JPA this class maps to a database table.

- `@Id` + `@GeneratedValue` makes `id` an auto-incrementing primary key.

- `@Column(nullable = false)` enforces required fields.

- `@NotBlank` triggers validation when we add `@Valid` in the controller.

---

## Step 5: Create the repository
Create a package `com.example.assettracker.repository` and an interface `AssetRepository.java`:

```
package com.example.assettracker.repository;

import com.example.assettracker.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
}
```
**What this does:** `JpaRepository` gives you `findAll()`, `findById()`, `save()`, `deleteById()`, and more — for free. No implementation needed. This is one of Spring Data's biggest wins.

---

## Step 6: Create the service layer
Create a package `com.example.assettracker.service` and a class `AssetService.java`.
**What this does:** the service layer holds business logic. Right now it's thin, but keeping it separate from the controller is good practice — later, if you add rules (e.g., "can't delete an assigned asset"), they live here.

**Why constructor injection?** It's the idiomatic Spring way to inject dependencies — clean, testable, and explicit.

---

## Step 7: Create the REST controller
Create a package `com.example.assettracker.controller` and a class `AssetController.java`.
**What this does:**

- `@RestController` — this class handles HTTP requests and returns JSON (Spring serializes it automatically).

- `@RequestMapping("/api/assets")` — all endpoints live under this base path.

- `@GetMapping`, `@PostMapping`, etc. — map HTTP verbs to methods.

- `@PathVariable` — binds `{id}` from the URL.

- `@RequestParam(required = false)` — binds `status` from the URL.

- `@RequestBody` — deserializes JSON from the request body into a Java object.

- `@Valid` — triggers bean validation (`@NotBlank` on the entity).

- `ResponseEntity` — gives you control over status codes and headers.

---

## Step 8: Run the application
From the project root:

```
bash
./mvnw spring-boot:run
```
(or `mvn spring-boot:run` if you have Maven installed globally)

You should see Spring Boot start up on port 8080. If you see `Started AssetTrackerApplication`, you're good.

---

## Step 9: Test the API
**Create an asset:**
```
bash
curl -X POST http://localhost:8080/api/assets \
  -H "Content-Type: application/json" \
  -d '{"name":"Dell Laptop","type":"LAPTOP","status":"IN_USE","assignedTo":"Alice"}'
```
Expected: a JSON response with an id and the fields you sent, status `201 Created`.

**List all assets:**
```
bash
curl http://localhost:8080/api/assets
```

**List all assets with status "IN_USE":**
```
bash
curl http://localhost:8080/api/assets?status=IN_USE
```

**Get one by ID:**
```
bash
curl http://localhost:8080/api/assets/1
```

**Update:**
```
bash
curl -X PUT http://localhost:8080/api/assets/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Dell Laptop","type":"LAPTOP","status":"IN_REPAIR","assignedTo":"Alice"}'
```

**Delete:**
```
bash
curl -X DELETE http://localhost:8080/api/assets/1
```

**Inspect the data in the H2 console:** open `http://localhost:8080/h2-console`, use JDBC URL `jdbc:h2:mem:assettracker`, username `sa`, empty password.

What You've Just Learned
By building this, you've now touched:

Concept | Where It Appeared
---|---
Spring Boot auto-configuration | The app "just works" with `application.properties`
Dependency injection | Constructor injection in service and controller
JPA / Hibernate	| `@Entity`, `@Id`, auto-generated tables
Spring Data repositories | `JpaRepository` gives CRUD for free
Layered architecture | Controller → Service → Repository
REST conventions | Proper verbs, status codes, resource paths
JSON serialization | Automatic via Jackson (bundled with Spring Web)
Bean validation | `@Valid` + `@NotBlank`
Maven | `pom.xml` manages all dependencies

That's a genuinely solid foundation for the rest of the project.