# Disaster Response System (DRS-Enhanced)
## COIT20258 Assignment 3 | Group 1

---

## Team Members

| Name | Student ID | Role |
|------|-----------|------|
| Alisha Koirala | 12268551 | Team Leader – Server & Business Logic |
| Shobiga Jeyasekar | 12269476 | JavaFX Client / GUI |
| Joyee Chakraborty | 12286715 | Database / DAO Layer / Testing |

## GitHub Repository
https://github.com/aleesakoirala/disaster-response-system

---

## System Overview

DRS-Enhanced is a three-tier distributed application:
- **Presentation Tier** — JavaFX client (FXML views + controllers)
- **Application Tier** — Multi-threaded socket server (port 6000) with business logic
- **Data Tier** — MySQL database accessed via JDBC DAO layer

---

## Prerequisites

- Java 21 or higher
- Apache Maven
- MySQL 8.0 or higher
- NetBeans IDE (or any IDE with Maven support)

---

## How to Run

### Step 1 — Configure MySQL credentials

Open `src/main/java/com/university/disasterresponsesystem/dao/Database.java`
and update the credentials to match your local MySQL:

```java
private static final String USER     = "root";
private static final String PASSWORD = "P@ssw0rd";
```

### Step 2 — Build the project

In NetBeans: right-click the project → **Clean and Build**

Or in Terminal / Command Prompt:
```
mvn clean install
```

Must say **BUILD SUCCESS** before continuing.

### Step 3 — Start the server

Right-click `server/ServerApp.java` → **Run File**

Wait until the console shows all four lines:
```
[Database] All tables verified/created.
Database initialised (tables ready).
Seeded default admin user: admin / admin123
DRS multi-threaded server starting on port 6000 ...
Server is listening. Waiting for clients.
```

Leave this running. Do not close it.

### Step 4 — Start the client

**Option A — In NetBeans (recommended, works on any OS):**

Right-click project → **Run Maven** → **Other Goals**
→ type `javafx:run` → click **OK**

**Option B — Mac/Linux Terminal:**
```
cd /path/to/disaster-response-system
mvn javafx:run
```

**Option C — Windows Command Prompt:**
```
cd C:\path\to\disaster-response-system
mvn javafx:run
```

The login window will open.

### Step 5 — Log in

```
Username: admin
Password: admin123
```

---

## Features

1. **Submit disaster reports** — report type, location, people affected
2. **Assess reports** — assign severity score, auto-generates prioritised incident
3. **Coordinate departments** — assign departments to incidents
4. **Resource Management** — register and dispatch resources to incidents (Feature 1)
5. **Alert Management** — create, view and clear public alerts (Feature 2)
6. **Role-based access** — Admin, Coordinator, Operator, Viewer
7. **Audit log** — every action is logged with username and timestamp
8. **Password security** — salted SHA-256 hashing (never stored in plain text)

---

## Database

Tables are created **automatically** on first server startup — no manual setup needed.

To set up manually using the provided scripts:

**Mac/Linux:**
```
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
```

**Windows:**
```
mysql -u root -p < database\schema.sql
mysql -u root -p < database\seed.sql
```

Tables created:
- `users` — system accounts with roles
- `disaster_reports` — submitted disaster reports
- `incidents` — assessed and prioritised incidents
- `alerts` — public alerts (Feature 2: Alert Management)
- `resources` — response resources (Feature 1: Resource Management)
- `audit_log` — non-repudiation audit trail

---

## Port Configuration

Default server port: **6000**

If port 6000 is in use, change it in two files:
- `src/main/java/com/university/disasterresponsesystem/server/DrsServer.java`
  → `DEFAULT_PORT = 6000`
- `src/main/java/com/university/disasterresponsesystem/client/net/ServerConnection.java`
  → `PORT = 6000`

Both must use the same port number.

---

## Running Tests

In NetBeans: right-click project → **Test**

Or in Terminal / Command Prompt:
```
mvn test
```

Test classes located in Test Packages:
- `AssessmentLogicTest` — priority level calculation
- `CoordinationLogicTest` — department recommendation by disaster type
- `PasswordUtilTest` — password hashing and verification

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `Communications link failure` | MySQL not running, or wrong credentials in `Database.java` |
| `Address already in use: 6000` | Mac/Linux: `kill -9 $(lsof -ti :6000)` — Windows: `netstat -ano | findstr :6000` then `taskkill /PID <pid> /F` |
| `JavaFX runtime components missing` | Use `mvn javafx:run` not Run File for the client |
| Login shows no response | Make sure server is running before starting the client |
| Tables not created | Check MySQL credentials and confirm MySQL service is running |
| `Seeded default admin` not shown | Database connection failed — check credentials and MySQL status |