# Employee Leave Management System (Spring Boot)

A simple REST API to manage employees and leave requests.

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Validation
- Spring Security (configured to allow all requests for now)
- H2 Database (in-memory)

## Features
- Create and view employees
- Apply for leave
- Approve or reject leave requests
- Auto leave balance deduction on approval
- Validation + centralized exception handling

## API Endpoints

### Employee APIs
- `POST /api/employees` - create employee
- `GET /api/employees` - list all employees
- `GET /api/employees/{id}` - get employee by id

### Leave APIs
- `POST /api/leaves` - apply leave
- `PUT /api/leaves/{id}/decision` - approve/reject leave
- `GET /api/leaves` - list all leaves
- `GET /api/leaves?employeeId={employeeId}` - list leaves by employee

## Sample Request Payloads

Create employee:
```json
{
  "name": "Alice",
  "email": "alice@company.com",
  "department": "Engineering",
  "leaveBalance": 20
}
```

Apply leave:
```json
{
  "employeeId": 1,
  "startDate": "2026-03-10",
  "endDate": "2026-03-12",
  "reason": "Family function"
}
```

Decision:
```json
{
  "status": "APPROVED",
  "comment": "Approved by manager"
}
```

## Run Locally
```bash
./mvnw spring-boot:run
```

H2 Console: `http://localhost:8080/h2-console`
