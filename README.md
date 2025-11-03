# Jonah Professional Portfolio

A curated multi-module Maven repository that demonstrates backend engineering, data processing, full-stack development, automation tooling, and cloud-native observability. Each module is production-ready with tests, documentation, and modern tooling choices.

## Modules

### 1. REST Task Service (`rest-task-service`)
A Spring Boot REST API for task management featuring validation, error handling, and end-to-end tests. It showcases clean architecture patterns and how to ship a resilient microservice quickly.

* **Tech**: Spring Boot 3, Jakarta Validation, MockMvc tests
* **Highlights**:
  * CRUD endpoints with validation and structured error responses
  * In-memory repository for demo purposes, easily replaceable with a database
  * Comprehensive integration tests using MockMvc

Run locally:
```bash
mvn -pl rest-task-service spring-boot:run
```

### 2. Data Analytics Pipeline (`data-analytics-pipeline`)
A batch processing module that ingests CSV data and computes revenue insights.

* **Tech**: Apache Commons CSV, SLF4J
* **Highlights**:
  * Declarative aggregation with Java Streams
  * Reusable `CsvSalesLoader` and `RevenueAggregator` components
  * Unit tests verifying numeric accuracy

Run the pipeline:
```bash
mvn -pl data-analytics-pipeline exec:java -Dexec.mainClass=com.jonah.portfolio.pipeline.PipelineRunner
```

### 3. Fullstack Expense Tracker (`fullstack-expense-tracker`)
An opinionated Spring Boot backend paired with a lightweight HTML/JS single-page UI for personal finance tracking.

* **Tech**: Spring Boot 3, Actuator, Validation, Vanilla JS
* **Highlights**:
  * REST API with summary endpoints for monthly totals and category breakdowns
  * Static UI served from the same app for a portable full-stack demo
  * WebMvc slice tests using Mockito

Run locally:
```bash
mvn -pl fullstack-expense-tracker spring-boot:run
```
Visit [http://localhost:8080](http://localhost:8080) for the UI.

### 4. DevOps Automation CLI (`devops-automation-cli`)
A Picocli-based command line that scans Maven projects for unstable dependency versions.

* **Tech**: Picocli, Maven Model API
* **Highlights**:
  * Detects missing, snapshot, and dynamic dependency versions
  * Useful for CI pipelines to enforce dependency hygiene
  * Includes fast unit tests operating on temporary POM files

Usage example:
```bash
mvn -pl devops-automation-cli -q exec:java \
  -Dexec.mainClass=com.jonah.portfolio.cli.DevopsAutomationCliApplication \
  -Dexec.args="pom.xml"
```

### 5. Cloud Native Observability (`cloud-native-observability`)
A reactive weather stream service instrumented with Micrometer tracing and OTLP exporters.

* **Tech**: Spring WebFlux, Micrometer, OpenTelemetry
* **Highlights**:
  * Server-sent events endpoint with reactive streams
  * Observation and tracing configuration suitable for cloud platforms
  * Reactor-based unit tests exercising asynchronous logic

Run locally:
```bash
mvn -pl cloud-native-observability spring-boot:run
```

## Development Tooling

* Java 17, managed via the parent Spring Boot BOM
* Maven Wrapper friendly `.gitignore`
* JUnit 5 with AssertJ for expressive tests
* JaCoCo and Surefire configured in the parent `pom.xml`

## Continuous Improvement Ideas

* Containerize each module with Dockerfiles and deploy via GitHub Actions
* Add database persistence (PostgreSQL) for the Task and Expense services
* Integrate Terraform or Pulumi examples for infrastructure automation
* Publish the CLI to Maven Central or a private artifact repository

## License

This project is licensed under the [MIT License](LICENSE).
