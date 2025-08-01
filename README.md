# Online Course Management System (OCMS)

A comprehensive online course management system built with Spring Boot, featuring module-based architecture and advanced data structures for efficient course management, assignment handling, and reporting.

## 🚀 Features

### Core Modules
- **User Management Module**: Registration, authentication, and role-based access control
- **Course Management Module**: CRUD operations for courses and modules with sequenced content
- **Assignment Management Module**: Assignment creation, submission, and grading with deadline prioritization
- **Reporting Module**: Analytics and reports for student performance and course completion

### Data Structures Implemented
- **LinkedList**: For managing course content in a sequenced manner
- **HashMap**: For storing user information and mapping courses to enrolled students
- **PriorityQueue**: For handling assignment submissions based on deadlines
- **TreeMap**: For generating reports and sorting data by dates or user activity

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT
- **Data Persistence**: JPA/Hibernate
- **Build Tool**: Maven
- **Documentation**: OpenAPI/Swagger

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 🚀 Quick Start

### 1. Database Setup

Create a MySQL database and update the configuration in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ocms_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: your_username
    password: your_password
```

### 2. Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd online-course-management-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 3. Database Initialization

The application will automatically create the database schema on startup. You can also manually create the database:

```sql
CREATE DATABASE ocms_db;
```

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT"
}
```

#### Login
```http
POST /api/users/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

Response includes JWT token for subsequent requests.

### Course Management Endpoints

#### Create Course
```http
POST /api/courses
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Introduction to Java Programming",
  "description": "Learn the basics of Java programming",
  "instructorId": 1
}
```

#### Get Course Modules (LinkedList)
```http
GET /api/courses/{courseId}/module-sequence
Authorization: Bearer <jwt_token>
```

#### Enroll Student
```http
POST /api/courses/{courseId}/enroll/{studentId}
Authorization: Bearer <jwt_token>
```

### Assignment Management Endpoints

#### Create Assignment
```http
POST /api/assignments
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Java Basics Quiz",
  "description": "Test your knowledge of Java fundamentals",
  "courseId": 1,
  "dueDate": "2024-12-31T23:59:59",
  "maxScore": 100
}
```

#### Submit Assignment (PriorityQueue)
```http
POST /api/assignments/submit
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "assignmentId": 1,
  "userId": 1,
  "content": "My assignment submission"
}
```

#### Grade Submission
```http
POST /api/assignments/submissions/{id}/grade
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "score": 85.5,
  "feedback": "Good work! Consider improving error handling."
}
```

### Reporting Endpoints

#### Generate Student Performance Report
```http
GET /api/reports/student-performance/{studentId}?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
Authorization: Bearer <jwt_token>
```

#### Generate Course Completion Report
```http
GET /api/reports/course-completion/{courseId}
Authorization: Bearer <jwt_token>
```

#### Generate Custom Report (TreeMap)
```http
POST /api/reports/custom
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "reportType": "student_performance",
  "userId": 1,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59"
}
```

## 🏗️ Project Structure

```
src/main/java/com/ocms/
├── common/
│   ├── datastructures/
│   │   ├── LinkedList.java
│   │   └── PriorityQueue.java
│   ├── dto/
│   │   └── ApiResponse.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ResourceNotFoundException.java
│       └── UnauthorizedException.java
├── user/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── course/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── assignment/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── reporting/
│   ├── controller/
│   ├── dto/
│   └── service/
└── security/
    ├── JwtAuthenticationFilter.java
    ├── JwtTokenProvider.java
    └── SecurityConfig.java
```

## 🔐 Security

The application uses JWT-based authentication with role-based access control:

- **STUDENT**: Can enroll in courses, submit assignments, view their progress
- **INSTRUCTOR**: Can create/manage courses, create assignments, grade submissions
- **ADMIN**: Full system access including user management and reporting

## 📊 Data Structures Usage

### LinkedList
- Used in course modules to maintain sequential order
- Ensures proper progression through course content
- Supports dynamic reordering of modules

### HashMap
- User cache for quick access to user information
- Course enrollment mapping for efficient lookups
- Reduces database queries for frequently accessed data

### PriorityQueue
- Assignment submissions prioritized by deadline
- Ensures timely processing of urgent submissions
- Supports efficient grading workflow

### TreeMap
- Activity logging sorted by timestamp
- Report generation with chronological ordering
- Time-series analytics for system monitoring

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

## 📈 Performance Features

- **Caching**: User and course enrollment data cached in memory
- **Lazy Loading**: JPA entities configured for optimal database queries
- **Connection Pooling**: HikariCP for efficient database connections
- **JWT Tokens**: Stateless authentication for scalability

## 🔧 Configuration

Key configuration options in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ocms_db
    username: root
    password: root
  
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24 hours
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions, please open an issue in the repository.

---

**Note**: This is a comprehensive educational project demonstrating advanced Spring Boot concepts, data structures, and module-based architecture. The system is designed for learning purposes and can be extended for production use with additional security measures and optimizations.