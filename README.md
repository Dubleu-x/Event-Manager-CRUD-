# Event Manager Backend System

A comprehensive Spring Boot backend application for managing events, users, and event applications with JWT authentication and role-based access control.

## 🚀 Features

### User Management
- **User Registration & Login** with JWT authentication
- **Role-based Access Control** (ADMIN and USER roles)
- **Admin User Management** - Create, view, update, and delete users

### Event Management
- **Event CRUD Operations** - Admin can create, read, update, and delete events
- **Available Events** - Public endpoint to view active events
- **Automatic Organizer Assignment** - Events are linked to the admin who created them

### Application Management
- **Event Applications** - Users can apply for events
- **Application Review** - Admin can approve or reject applications
- **Status Tracking** - PENDING, APPROVED, REJECTED statuses
- **Duplicate Prevention** - Users can apply only once per event

### Security
- **JWT Authentication** with Bearer tokens
- **Password Encryption** using BCrypt
- **Role-based Endpoint Protection**
- **CORS Configuration**

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** (Hibernate)
- **Spring Security** with JWT
- **MySQL Database**
- **Lombok** for boilerplate reduction
- **Maven** for dependency management
- **JJWT** for JWT token handling

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java 17** or higher
- **MySQL Server 8.0** or higher
- **Maven 3.6** or higher
- **Git** (optional)

## 🗄️ Database Setup

1. **Create MySQL Database**:
```sql
CREATE DATABASE event_manager;
```

2. **Update database configuration** in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/event_manager
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

## 🚀 Installation & Running

### Step 1: Clone and Setup
```bash
# Clone the repository (if using Git)
git clone <repository-url>
cd eventmanager

# Or extract the project files to eventmanager directory
```

### Step 2: Configure Database
Update the database credentials in `src/main/resources/application.properties`

### Step 3: Build the Application
```bash
mvn clean compile
```

### Step 4: Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔐 Default Users

On first startup, the system automatically creates:

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: `ADMIN`
- **Email**: `admin@eventmanager.com`

### Test User (Optional)
You can register additional users through the API.

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and receive JWT token |

### User Management Endpoints (Admin Only)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| POST | `/api/users` | Create a new user |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user details |
| DELETE | `/api/users/{id}` | Delete a user |

### Event Management Endpoints

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/events/available` | Public | Get active events |
| GET | `/api/events` | ADMIN | Get all events |
| POST | `/api/events` | ADMIN | Create a new event |
| GET | `/api/events/{id}` | ADMIN, USER | Get event by ID |
| PUT | `/api/events/{id}` | ADMIN | Update event |
| DELETE | `/api/events/{id}` | ADMIN | Delete event |

### Application Endpoints

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/applications/apply/{eventId}` | USER | Apply for event |
| GET | `/api/applications/my-applications` | USER | Get user's applications |
| GET | `/api/applications` | ADMIN | Get all applications |
| PUT | `/api/applications/{id}/approve` | ADMIN | Approve application |
| PUT | `/api/applications/{id}/reject` | ADMIN | Reject application |

## 🧪 Testing with Postman

### 1. Import Postman Collection
1. Open Postman
2. Import the provided Postman collection JSON file
3. Set up environment variables:
   - `baseUrl`: `http://localhost:8080`

### 2. Test Sequence

#### Step 1: Authentication
```bash
# Register a new user
POST /api/auth/register
Body: {
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}

# Login as user
POST /api/auth/login
Body: {
  "username": "testuser",
  "password": "password123"
}
Save the JWT token for future requests
```

#### Step 2: Admin Operations
```bash
# Login as admin
POST /api/auth/login
Body: {
  "username": "admin",
  "password": "admin123"
}
Save the admin JWT token

# Create events
POST /api/events
Headers: Authorization: Bearer {admin-token}
Body: {
  "title": "Spring Boot Workshop",
  "description": "Learn Spring Boot",
  "expiryDate": "2024-12-31"
}
```

#### Step 3: User Operations
```bash
# View available events
GET /api/events/available

# Apply for event
POST /api/applications/apply/1
Headers: Authorization: Bearer {user-token}

# View user applications
GET /api/applications/my-applications
Headers: Authorization: Bearer {user-token}
```

#### Step 4: Admin Management
```bash
# View all applications
GET /api/applications
Headers: Authorization: Bearer {admin-token}

# Approve application
PUT /api/applications/1/approve
Headers: Authorization: Bearer {admin-token}

# View all users
GET /api/users
Headers: Authorization: Bearer {admin-token}
```

## 🔧 Configuration

### Application Properties
Key configuration in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/event_manager
spring.datasource.username=root
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=eventManagerSecretKey2024ForJWTTokenGeneration
jwt.expiration=86400000

# Server Configuration
server.port=8080

# Logging
logging.level.com.eventmanager=DEBUG
```

### JWT Configuration
- **Secret Key**: Configured in application.properties
- **Expiration**: 24 hours (86400000 milliseconds)
- **Algorithm**: HS256

## 🗃️ Database Schema

### Users Table
| Field | Type | Description |
|-------|------|-------------|
| id | BIGINT | Primary Key, Auto Increment |
| username | VARCHAR(255) | Unique username |
| email | VARCHAR(255) | Unique email address |
| password | VARCHAR(255) | Encrypted password |
| role | ENUM | ADMIN or USER |
| created_date | TIMESTAMP | Account creation timestamp |

### Events Table
| Field | Type | Description |
|-------|------|-------------|
| id | BIGINT | Primary Key, Auto Increment |
| title | VARCHAR(255) | Event title |
| description | TEXT | Event details |
| upload_date | DATE | Event creation date |
| expiry_date | DATE | Event expiry date |
| organizer_id | BIGINT | Foreign Key to Users (Admin) |

### Event_Applications Table
| Field | Type | Description |
|-------|------|-------------|
| id | BIGINT | Primary Key, Auto Increment |
| event_id | BIGINT | Foreign Key to Events |
| user_id | BIGINT | Foreign Key to Users |
| application_date | TIMESTAMP | Application submission time |
| status | ENUM | PENDING, APPROVED, or REJECTED |

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in application.properties
   - Ensure database `event_manager` exists

2. **Compilation Errors**
   - Ensure Java 17 is installed: `java -version`
   - Clean and rebuild: `mvn clean compile`

3. **JWT Authentication Issues**
   - Verify token is included in Authorization header
   - Check token expiration
   - Ensure secret key is configured

4. **Role-based Access Denied**
   - Verify user has correct role for the endpoint
   - ADMIN role required for user management endpoints

5. **Duplicate Application Error**
   - Users can apply only once per event
   - Check if user has already applied

### Logs and Debugging
- Check application logs for detailed error messages
- Enable debug logging in application.properties
- Verify JWT token generation and validation

## 📁 Project Structure

```
src/main/java/com/eventmanager/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java # Security configuration
│   └── JwtAuthenticationFilter.java # JWT filter
├── controller/            # REST controllers
│   ├── AuthController.java
│   ├── UserController.java
│   ├── EventController.java
│   └── ApplicationController.java
├── dto/                  # Data Transfer Objects
│   ├── AuthDTO.java
│   ├── UserDTO.java
│   ├── EventDTO.java
│   └── ApplicationDTO.java
├── entity/               # JPA entities
│   ├── User.java
│   ├── Event.java
│   └── Application.java
├── repository/           # Data access layer
│   ├── UserRepository.java
│   ├── EventRepository.java
│   └── ApplicationRepository.java
├── service/              # Business logic
│   ├── AuthService.java
│   ├── UserService.java
│   ├── EventService.java
│   └── ApplicationService.java
├── util/                 # Utility classes
│   └── JwtUtil.java      # JWT utility
└── EventManagerApplication.java # Main application class
```

## 🔮 Future Enhancements

- Email notifications for application status updates
- File upload for event images
- Advanced search and filtering
- Pagination for large datasets
- Swagger/OpenAPI documentation
- Docker containerization
- Unit and integration tests
- Frontend React application

## 👥 API Response Examples

### Successful Login Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 2,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER",
  "message": "Login successful"
}
```

### Event Response
```json
{
  "id": 1,
  "title": "Spring Boot Workshop",
  "description": "Learn Spring Boot fundamentals",
  "uploadDate": "2024-11-16",
  "expiryDate": "2024-12-31",
  "organizerId": 1,
  "organizerName": "admin"
}
```

### Application Response
```json
{
  "id": 1,
  "eventId": 1,
  "eventTitle": "Spring Boot Workshop",
  "userId": 2,
  "userName": "testuser",
  "userEmail": "test@example.com",
  "applicationDate": "2024-11-16T10:30:00.12345",
  "status": "APPROVED"
}
```

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Verify database connectivity
4. Ensure all dependencies are correctly configured

## 📄 License

This project is licensed under the MIT License.

