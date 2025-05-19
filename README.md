# Vehicle Workshop Management API

This project provides a Spring Boot (Java 21)–powered REST API to manage a vehicle workshop’s inventory, supporting diesel, gasoline and electric vehicles (including planned conversions from electric to gasoline). PostgreSQL is used as the backing database, run via Docker Compose.

---

## 📋 Prerequisites

- **Java 21** (JDK)
- **Maven** (bundled wrapper `./mvnw` can be used)
- **Docker & Docker Compose**

---

## ⚙️ Configuration

1. **Database**  
   Edit `docker-compose.yml` if you need to change:
   - Database name (default: `workshop`)
   - Username/password (`postgres` / `your_password`)
   - Port mapping (`5432:5432`)

2. **Spring Boot**  
   In `src/main/resources/application.properties` configure:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/workshop
   spring.datasource.username=postgres
   spring.datasource.password=your_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   spring.jackson.serialization.write-dates-as-timestamps=false
   server.port=8080
```

---

## 🚀 Running Locally

1. **Start PostgreSQL**

   ```bash
   docker-compose up -d
   ```

2. **Build & run the API**

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   The application will start on **[http://localhost:8080/](http://localhost:8080/)**.

---

## 🛠️ Available Endpoints

Once running, you can interact with the API via Swagger UI:

* **Swagger UI**:
  `http://localhost:8080/swagger-ui.html` or
  `http://localhost:8080/swagger-ui/index.html`

### Vehicles

| Method | Path                                   | Description                                          |                        |
| ------ | -------------------------------------- | ---------------------------------------------------- | ---------------------- |
| GET    | `/api/vehicles`                        | List all vehicles                                    |                        |
| GET    | \`/api/vehicles?type={DIESEL           | ...}\`                                               | Filter by vehicle type |
| POST   | `/api/vehicles`                        | Register a new vehicle (supports planned conversion) |                        |
| DELETE | `/api/vehicles/{id}`                   | Remove a vehicle from inventory                      |                        |
| GET    | `/api/vehicles/{id}`                   | Get full details of one vehicle                      |                        |
| GET    | `/api/vehicles/{id}/registration-info` | Get encoded registration & conversion info           |                        |
| GET    | `/api/vehicles/registration-info`      | Get encoded registration & conversion for all        |                        |

---

## ✅ Testing

Run the integration and unit tests:

```bash
./mvnw test
```

---

## 🚧 Notes & Tips

* **Schema migrations**: consider adding Flyway or Liquibase for production.
* **Validation**: Bean Validation + custom exception handlers provide clear, localized error responses.
* **Extensibility**: MapStruct mappers generate DTO conversions; feel free to extend or replace with manual logic.

