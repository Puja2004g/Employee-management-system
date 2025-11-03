## Employee Management System (Java, Swing, Hibernate, MySQL)

An Employee Management System featuring both a desktop GUI (Swing) and a CLI menu, backed by Hibernate ORM and a MySQL database.

### Features
- **Employees**: create, read, update, delete; search by name or ID
- **Employment Records**: track start year and end year ("Present" if still employed)
- **Deletion rule**: deleting an employee auto-sets all open records' end year to current year
- **Two UIs** with startup chooser:
  - **GUI**: `com.example.ems.SwingApp` launches a module chooser (Employees or Employment Records)
  - **CLI**: `com.example.ems.App` runs an interactive console menu (employees)

### Tech Stack
- **Language**: Java 17
- **Build**: Maven
- **ORM**: Hibernate 6
- **DB**: MySQL (auto schema update enabled)
- **UI**: Java Swing
- **Logging**: SLF4J Simple
- **Tests**: JUnit 5

### Project Structure
```
src/
  main/
    java/com/example/ems/
      App.java                      # CLI entry point
      SwingApp.java                 # GUI entry point (default exec)
      entity/Employee.java          # JPA entity
      repository/EmployeeRepository.java  # Data access (Hibernate Session API)
      repository/EmploymentRecordRepository.java  # Save employment records
      ui/EmployeeManagementFrame.java     # Swing UI
      ui/EmploymentRecordFrame.java       # Swing UI for employment records
      util/HibernateUtil.java       # SessionFactory bootstrap
    resources/hibernate.cfg.xml     # Hibernate + DB configuration
  test/java/com/example/AppTest.java
pom.xml                             # Maven config
```

### Prerequisites
- Java 17 (JDK)
- Maven 3.9+
- MySQL Server running locally and accessible

### Database Configuration
The application uses MySQL with the following defaults (defined in `src/main/resources/hibernate.cfg.xml`):
- URL: `jdbc:mysql://localhost:3306/ems?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- Username: `root`
- Password: `123456`
- Dialect: `org.hibernate.dialect.MySQLDialect`
- Schema strategy: `hibernate.hbm2ddl.auto = update` (creates/updates tables automatically)

Steps:
1) Create the database:
```sql
CREATE DATABASE ems CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
2) If needed, edit credentials/URL in `src/main/resources/hibernate.cfg.xml` to match your environment.

### Build
```bash
mvn clean package
```

### Run (GUI)
The `pom.xml` configures the executable main class as `com.example.ems.SwingApp`. Run:
```bash
mvn exec:java
```
On launch, a chooser dialog appears: pick **Employees** or **Employment Records**. Use the Back option in either screen to return to the chooser.

Alternatively, run directly with `java` after building:
```bash
mvn -DskipTests package
java -cp target/classes;target/dependency/* com.example.ems.SwingApp   # (Windows PowerShell)
```

### Run (CLI)
Common operations available in the menu:
- Add employee
- Update employee
- Delete employee
- Show by ID
- List all employees
- Find employees by name (contains)

### Using the GUI
- On start, select a module:
  - **Employees**: manage employee records
  - **Employment Records**: add employment tenure entries
- Each screen includes a Back option to return to the chooser.

Employees screen:
- The top form lets you enter employee details (First/Last/Email/Department).
- Table displays existing employees; selecting a row populates the form for editing.
- Actions: Add / Update / Delete / Refresh / Find by Name / Find by ID / Back

Employment Records screen:
- Enter either Employee ID OR both First Name and Last Name.
- Provide Start Year (required). Check "Still employed" to set end year to "Present" automatically; otherwise current year is used.
- Saves only if the employee exists; exact name match is required when using names. If multiple matches, the UI asks to use the Employee ID.

### Entity Model
`Employee` fields:
- `id` (Long, auto-generated)
- `firstName` (required)
- `lastName` (required)
- `email` (required, unique)
- `department`

`EmploymentRecord` fields:
- `id` (Long, auto-generated)
- `employee` (Many-to-one to `Employee`)
- `employeeName` (snapshot of name at record time)
- `startYear` (Integer, required)
- `endYear` (String: year like "2024" or the literal "Present" for ongoing employment)
- Derived helpers: `getYearsWorked()`, `getPeriod()`

### Repository
`EmployeeRepository` provides:
- `save(Employee)`
- `findById(Long)`
- `findAll()`
- `findByName(String)` — case-insensitive contains on first/last name
- `findByFirstAndLast(String, String)` — exact case-insensitive match on first and last names
- `update(Employee)`
- `delete(Long)`

`EmploymentRecordRepository` provides:
- `save(EmploymentRecord)`

### Configuration Notes
- SessionFactory is built via `HibernateUtil` using `hibernate.cfg.xml` on the classpath.
- A JVM shutdown hook closes Hibernate when the GUI app exits.
 - `hibernate.cfg.xml` maps both `Employee` and `EmploymentRecord`.

### Testing
```bash
mvn test
```

### Troubleshooting
- "Access denied" or connection errors: verify MySQL is running, DB exists, and credentials/URL in `hibernate.cfg.xml` are correct.
- Schema issues: `hibernate.hbm2ddl.auto=update` should create/update the `employees` table automatically.
- Port conflicts: ensure nothing else uses MySQL port 3306 or adjust the JDBC URL accordingly.
 - Bulk update uses Hibernate 6 API (`createMutationQuery`) to avoid deprecations.

### Notes
- The Maven `artifactId`/`name` in `pom.xml` currently say "grading-system" but the codebase is an Employee Management System. You may rename those fields if desired.


