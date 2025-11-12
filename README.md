# Event Manager Backend System

A comprehensive Spring Boot backend application for managing events, users, and event applications with role-based access control.

## 🚀 Features

### User Management
- **Admin Role**: Create, view, update, and delete users
- **User Role**: View active events and apply for events
- **Authentication**: Spring Security with Basic Auth
- **Role-based Access Control**: ADMIN and USER roles

### Event Management
- **Admin Role**: Create, view, update, and delete events
- **Automatic Organizer Assignment**: Events are linked to the admin who created them
- **Active Events Filter**: Users can view only active events (not expired)

### Security
- Spring Security with BCrypt password encoding
- Role-based endpoint protection
- Pre-configured admin user on startup

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Database**
- **Lombok**
- **Maven**

## 📋 Prerequisites

Before running this application, ensure you have:

- Java 17 or higher
- MySQL Server 8.0 or higher
- Maven 3.6 or higher

## 🗄️ Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE event_manager;
```

2. Update the database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/event_manager
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 🚀 Installation & Running

1. **Clone the repository** (if applicable) or navigate to the project directory

2. **Compile the project**:
```bash
mvn clean compile
```

3. **Run the application**:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔐 Default Admin User

On first startup, the system automatically creates an admin user:
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `ADMIN`

## 📚 API Documentation

### Authentication
All endpoints use **HTTP Basic Authentication**. Include credentials in the request header.

### User Management Endpoints (Admin Only)

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/users` | Create a new user | ADMIN |
| GET | `/api/users` | Get all users | ADMIN |
| GET | `/api/users/{id}` | Get user by ID | ADMIN |
| PUT | `/api/users/{id}` | Update user details | ADMIN |
| DELETE | `/api/users/{id}` | Delete a user | ADMIN |

#### User Request Examples:

**Create User:**
```bash
curl -u admin:admin123 -X POST -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "USER"
  }' \
  http://localhost:8080/api/users
```

**Get All Users:**
```bash
curl -u admin:admin123 http://localhost:8080/api/users
```

### Event Management Endpoints

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/events` | Create a new event | ADMIN |
| GET | `/api/events` | Get all events | ADMIN |
| GET | `/api/events/active` | Get active events | ADMIN, USER |
| GET | `/api/events/{id}` | Get event by ID | ADMIN, USER |
| PUT | `/api/events/{id}` | Update event details | ADMIN |
| DELETE | `/api/events/{id}` | Delete an event | ADMIN |

#### Event Request Examples:

**Create Event:**
```bash
curl -u admin:admin123 -X POST -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Conference 2024",
    "description": "Annual Spring Developer Conference",
    "expiryDate": "2024-12-31"
  }' \
  http://localhost:8080/api/events
```

**Get Active Events:**
```bash
curl -u admin:admin123 http://localhost:8080/api/events/active
```

## 🗃️ Database Schema

### Users Table
| Field | Type | Description |
|-------|------|-------------|
| id | Long | Primary Key |
| username | String | Unique username |
| email | String | Unique email address |
| password | String | Encrypted password |
| role | Enum | ADMIN or USER |
| createdDate | LocalDateTime | Account creation timestamp |

### Events Table
| Field | Type | Description |
|-------|------|-------------|
| id | Long | Primary Key |
| title | String | Event title |
| description | Text | Event details |
| uploadDate | LocalDate | Event creation date |
| expiryDate | LocalDate | Event expiry date |
| organizer_id | Long | Foreign Key to Users (Admin) |

## 🔧 Configuration

### Application Properties
Key configuration in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/event_manager
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Server
server.port=8080

# Security (temporary - for testing)
spring.security.user.name=admin
spring.security.user.password=admin123
```

## 🧪 Testing the API

### Using curl:

1. **Test authentication**:
```bash
curl -u admin:admin123 http://localhost:8080/api/users
```

2. **Create a test user**:
```bash
curl -u admin:admin123 -X POST -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"testpass","role":"USER"}' \
  http://localhost:8080/api/users
```

3. **Create a test event**:
```bash
curl -u admin:admin123 -X POST -H "Content-Type: application/json" \
  -d '{"title":"Test Event","description":"This is a test event","expiryDate":"2024-12-31"}' \
  http://localhost:8080/api/events
```

### Using Postman:

1. Set authentication to **Basic Auth** with username `admin` and password `admin123`
2. Use the endpoints listed above with appropriate HTTP methods and JSON bodies

## 📁 Project Structure

```
src/main/java/com/eventmanager/
├── config/           # Security configuration
├── controller/       # REST controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── repository/      # Data access layer
├── service/         # Business logic
└── EventManagerApplication.java
```

## 🐛 Troubleshooting

### Common Issues:

1. **Database Connection Error**:
   - Verify MySQL is running
   - Check database credentials in `application.properties`

2. **Compilation Errors**:
   - Ensure Java 17 is installed
   - Run `mvn clean compile`

3. **Authentication Failures**:
   - Verify credentials are correct
   - Check that admin user was created on startup

4. **Role-based Access Denied**:
   - Ensure user has correct role for the endpoint
   - ADMIN role required for user management endpoints

## 📝 Development Notes

- The system uses Hibernate's `ddl-auto=update` for automatic schema generation
- Passwords are encrypted using BCrypt
- All timestamps are in server's local timezone
- Events are automatically assigned to the currently authenticated admin

## 🔮 Future Enhancements

- Event applications management (Part 2)
- Email notifications
- File upload for event images
- Advanced search and filtering
- Pagination for large datasets
- Swagger/OpenAPI documentation

## 👥 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

---

**Note**: This is Part 1 of the Event Manager system, focusing on User and Event management. Part 2 will include Event Applications functionality.