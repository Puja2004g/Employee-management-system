## Employee Management System (Java, Swing, Hibernate, MySQL)

An Employee Management System featuring both a desktop GUI (Swing) and a CLI menu, backed by Hibernate ORM and a MySQL database.

### Features
- **CRUD employees**: create, read, update, delete
- **Search**: by name (contains) and by ID
- **Two UIs**:
  - **GUI**: `com.example.ems.SwingApp` launches a Swing desktop app
  - **CLI**: `com.example.ems.App` runs an interactive console menu

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
      ui/EmployeeManagementFrame.java     # Swing UI
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
- The top form lets you enter employee details (First/Last/Email/Department).
- Table displays existing employees; selecting a row populates the form for editing.
- Bottom actions:
  - Add / Update / Delete
  - Refresh
  - Find by Name / Find by ID

### Entity Model
`Employee` fields:
- `id` (Long, auto-generated)
- `firstName` (required)
- `lastName` (required)
- `email` (required, unique)
- `department`

### Repository
`EmployeeRepository` provides:
- `save(Employee)`
- `findById(Long)`
- `findAll()`
- `findByName(String)` — case-insensitive contains on first/last name
- `update(Employee)`
- `delete(Long)`

### Configuration Notes
- SessionFactory is built via `HibernateUtil` using `hibernate.cfg.xml` on the classpath.
- A JVM shutdown hook closes Hibernate when the GUI app exits.

### Testing
```bash
mvn test
```

### Troubleshooting
- "Access denied" or connection errors: verify MySQL is running, DB exists, and credentials/URL in `hibernate.cfg.xml` are correct.
- Schema issues: `hibernate.hbm2ddl.auto=update` should create/update the `employees` table automatically.
- Port conflicts: ensure nothing else uses MySQL port 3306 or adjust the JDBC URL accordingly.

### Notes
- The Maven `artifactId`/`name` in `pom.xml` currently say "grading-system" but the codebase is an Employee Management System. You may rename those fields if desired.


