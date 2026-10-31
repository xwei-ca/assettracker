### Project: "Asset Tracker" — A Small Enterprise-Style Web Application

## Why this project

It's simple enough to finish, but it naturally exercises every major piece of the stack: a Java backend with Spring, SOAP and REST endpoints, XML/JSON serialization, a GWT-style frontend with AJAX calls, a relational database, Maven build, unit tests, and Selenium UI tests — all deployable as a Docker container.

A simple domain (tracking company assets: laptops, monitors, licenses) keeps the focus on learning the plumbing, not on business complexity.

Architecture Overview
text
[Browser: GWT/GXT frontend]
        │  (AJAX / JSON over REST)
        ▼
[Spring Boot app on Tomcat]
   ├── REST controllers (JSON)      ← consumed by the UI
   ├── SOAP web service (JAXB/XML)  ← "external system" integration
   ├── Service layer (business logic)
   └── Repository layer (JPA → H2/PostgreSQL)
        │
        ▼
   [Relational Database]
Plus:

Maven builds the whole thing

JUnit tests the service and controller layers

Selenium drives the browser against the running app

Docker packages it for deployment

Phase-by-Phase Build Plan
Phase 1 — Backend foundation (Spring Boot + REST + JPA)
Goal: a working REST API backed by a database.

Create a Spring Boot project (Spring Initializr) with Maven, Spring Web, Spring Data JPA, and H2 (or PostgreSQL).

Define an Asset entity (id, name, type, status, assignedTo).

Build a repository and a service layer.

Expose REST endpoints: GET /api/assets, GET /api/assets/{id}, POST /api/assets, PUT, DELETE.

Return JSON; verify with Postman or curl.

What you'll learn: how Spring Boot wires controllers → services → repositories, how JSON serialization works out of the box, and how Maven manages dependencies.

Phase 2 — SOAP web service with JAXB
Goal: add a second API style, as real enterprises often run both.

Add Spring Web Services.

Define an XSD schema for a simple operation (e.g., GetAssetRequest / GetAssetResponse).

Generate JAXB classes from the XSD (the jaxb2-maven-plugin does this).

Implement a SOAP endpoint that returns asset data as XML.

Test it with SoapUI or curl.

What you'll learn: the difference between REST/JSON and SOAP/XML, how JAXB maps XML to Java objects, and why enterprises still use SOAP for legacy integrations.

Phase 3 — GWT/GXT-style frontend with AJAX
Goal: a browser UI that calls the REST API asynchronously.

A note: GWT/GXT is aging and painful to set up from scratch today. For learning purposes, you have two options:

Option A (closer to the requirement): Build a small GWT app (GWT 2.10+ still works) with a simple table and form, using GWT-RPC or RequestBuilder for AJAX calls to your REST API.

Option B (more practical): Build the frontend in plain JavaScript + HTML + CSS using fetch() for AJAX calls and JSON parsing. This teaches the same concepts (async calls, JSON handling, DOM updates) without the GWT setup friction.

My recommendation: do Option B first to understand AJAX/JSON quickly, then spend a day on Option A just to see how GWT compiles Java to JavaScript. That gives you real familiarity with both without sinking weeks into GWT tooling.

What you'll learn: how a browser talks to a backend asynchronously, how JSON is parsed and rendered, and (with GWT) how Java can be compiled to JavaScript.

Phase 4 — Testing: JUnit + Selenium
Goal: automated tests at two levels.

JUnit: unit-test the service layer (mock the repository with Mockito) and write integration tests for the REST controllers using @SpringBootTest and MockMvc.

Selenium: write a small test that opens the UI in a browser, adds an asset, and verifies it appears in the table. Run it against the locally running app.

What you'll learn: the difference between unit, integration, and UI tests — and why enterprises invest in all three.

Phase 5 — Docker + Tomcat deployment
Goal: package and run the app as a container.

Write a Dockerfile (Spring Boot can build a runnable JAR; run it on a Tomcat base image or use the embedded Tomcat).

Build the image with Maven (mvn spring-boot:build-image or a manual Dockerfile).

Run it with docker run, expose the port, and confirm the app works in the container.

Optionally, add docker-compose.yml with the app + a PostgreSQL container.

What you'll learn: how containerization works in practice, how the app behaves outside your IDE, and the basics of orchestration-ready packaging.

On Hyper-V: you don't need it for this project, but if you're on Windows, you can note that Docker Desktop uses Hyper-V (or WSL2) as its virtualization layer — that's a legitimate, discussable connection to the requirement.

Phase 6 (optional) — CI/CD with GitLab
Goal: see how the pieces automate.

Push the project to GitLab.

Add a .gitlab-ci.yml that runs mvn test on every push.

Bonus: add a stage that builds the Docker image.

What you'll learn: how continuous integration actually runs, and how Maven, JUnit, and Docker fit into a pipeline.

