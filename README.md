# ErconAdjustment Task Tracker

Spring Boot app (REST backend + static HTML/JS frontend in one WAR) for tracking
tasks, assignments, man-days, and progress across systems for Prem, Venkat, and Marvin.

## 1. Prerequisites

- JDK 17 (or JDK 11 — see the Jakarta/JBoss version note below if your JBoss is older/newer)
- Maven 3.8+
- A running MSSQL Server with the `ErconAdjustment` schema already created
  (run `task_management_schema.sql` from the earlier step first)
- A JBoss EAP / WildFly instance to deploy into

## 2. Configure the database connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://YOUR_DB_HOST:1433;databaseName=TaskManagementDB;encrypt=true;trustServerCertificate=true
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
```

`spring.jpa.hibernate.ddl-auto=validate` is intentional — this app expects the schema
to already exist (from the SQL script) and will fail fast if the tables don't match,
rather than silently altering your database.

## 3. Build the WAR

```bash
mvn clean package
```

This produces `target/taskmanagement.war`.

## 4. Deploy to JBoss

Copy the WAR into JBoss's deployments folder:

```bash
cp target/taskmanagement.war $JBOSS_HOME/standalone/deployments/
```

JBoss auto-deploys it. Watch `standalone/log/server.log` for:

```
WFLYSRV0010: Deployed "taskmanagement.war"
```

The app will be reachable at:

```
http://<jboss-host>:8080/taskmanagement/
```

(The path comes from `src/main/webapp/WEB-INF/jboss-web.xml` — edit `<context-root>`
there if you want it at a different path, e.g. `/` for the server root.)

### Deploying via the JBoss CLI instead

```bash
$JBOSS_HOME/bin/jboss-cli.sh --connect \
  --command="deploy target/taskmanagement.war"
```

To redeploy after a rebuild:

```bash
$JBOSS_HOME/bin/jboss-cli.sh --connect \
  --command="undeploy taskmanagement.war"
$JBOSS_HOME/bin/jboss-cli.sh --connect \
  --command="deploy target/taskmanagement.war"
```

## 5. javax vs jakarta namespace (important)

This project is built with **Spring Boot 2.7.x**, which uses the `javax.*` package
namespace (`javax.persistence`, `javax.validation`, etc.). That matches:

- JBoss EAP 7.x
- WildFly up to version 26

If you're deploying to **WildFly 27+** (which moved to the `jakarta.*` namespace),
you'll need to either:

1. Upgrade to **Spring Boot 3.x** and change every `javax.persistence.*` /
   `javax.validation.*` import in the `entity/` and `dto/` packages to `jakarta.*`, or
2. Run WildFly in "javax compatibility mode" if your version still supports it.

## 6. Running locally without JBoss (for development)

You don't need JBoss to develop/test locally — Spring Boot's embedded Tomcat works too:

```bash
mvn spring-boot:run
```

Then open http://localhost:8080/ (the embedded-server context path is `/`, unlike
the JBoss deployment which uses `/taskmanagement` from jboss-web.xml).

## 7. Project layout

```
taskmanagement/
├── pom.xml                                  Maven build (packaging=war)
├── README.md
├── src/main/java/com/erconadjustment/taskmanagement/
│   ├── TaskManagementApplication.java       Main Spring Boot class
│   ├── ServletInitializer.java              WAR entry point for JBoss
│   ├── entity/                              JPA entities (7 tables)
│   ├── repository/                          Spring Data JPA repositories
│   ├── service/                             Business logic
│   ├── controller/                          REST endpoints (/api/**)
│   ├── dto/                                 Request payloads with validation
│   └── exception/                           Centralized error handling
├── src/main/resources/
│   ├── application.properties               DB connection + JPA config
│   └── static/                              Frontend (served at /)
│       ├── index.html
│       ├── css/style.css
│       └── js/app.js
└── src/main/webapp/WEB-INF/
    └── jboss-web.xml                        Sets the deployed context root
```

## 8. REST API summary

| Resource | Endpoints |
|---|---|
| Employees | `GET/POST /api/employees`, `GET/PUT/DELETE /api/employees/{id}` |
| Systems | `GET/POST /api/systems`, `GET/PUT/DELETE /api/systems/{id}` |
| Tasks | `GET/POST /api/tasks` (supports `?systemId=`), `GET/PUT/DELETE /api/tasks/{id}` |
| Assignments | `GET/POST /api/assignments` (supports `?taskId=` or `?employeeId=`), `GET/PUT/DELETE /api/assignments/{id}` |
| Progress log | `GET/POST /api/progress` (supports `?assignmentId=`), `GET/DELETE /api/progress/{id}` |
| Lookups | `GET /api/lookups/statuses`, `GET /api/lookups/priorities` |

All endpoints return/accept JSON. Validation errors return `400` with a message;
missing records return `404`.
