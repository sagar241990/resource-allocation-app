# ProjectAllocationApp

ProjectAllocationApp is a Spring Boot service for managing and allocating resources across projects. The service supports uploading CSV or Excel files containing allocation information and persists the data in an in-memory H2 database. A future Angular frontend will consume this backend.

## Tech Stack
- **Spring Boot** (web & data JPA)
- **H2 Database** for development and testing
- **Apache POI** for Excel file parsing

## Key Features
- Upload project allocation details using `/api/upload`
- Supports `.csv` and `.xlsx` files with the following columns:
  - `workRequestNumber`
  - `wrName`
  - `soeId`
  - `projectName`
  - `projectType`
  - `lob`
- Data from uploaded files is stored in the H2 database

## Development Tasks
1. Ensure dependencies for **Web**, **JPA**, and **H2** are present.
2. Organize the code base with packages for controller, service, repository, model, dto, and util.
3. Implement the `ProjectAllocation` JPA entity and repository.
4. Configure H2 database properties.
5. Implement CSV and Excel parsing logic in a generic upload service.
6. Validate uploaded data before persisting.

## Running the Application
Use Maven to start the application:

```bash
./mvnw spring-boot:run
```

The H2 console is available at `/h2-console` while the application is running.

