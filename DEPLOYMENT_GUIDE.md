# Online Course Management System (OCMS) - Deployment Guide

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 (for production) or H2 (for development)

### Development Setup (H2 Database)

1. **Clone the repository and navigate to the project directory**
   ```bash
   cd /workspace
   ```

2. **Run the application with development profile**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. **Access the application**
   - API Base URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:ocms_db`
     - Username: `sa`
     - Password: (leave empty)

### Production Setup (MySQL Database)

1. **Install and configure MySQL**
   ```bash
   sudo apt update
   sudo apt install mysql-server
   sudo systemctl start mysql
   sudo systemctl enable mysql
   ```

2. **Create database and user**
   ```sql
   CREATE DATABASE ocms_db;
   CREATE USER 'ocms_user'@'localhost' IDENTIFIED BY 'your_secure_password';
   GRANT ALL PRIVILEGES ON ocms_db.* TO 'ocms_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Update application.yml with your MySQL credentials**
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/ocms_db
       username: ocms_user
       password: your_secure_password
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## 🧪 Testing the Application

### 1. Register Users

**Register a Student:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "email": "student1@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT"
  }'
```

**Register an Instructor:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "instructor1",
    "email": "instructor1@example.com",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Smith",
    "role": "INSTRUCTOR"
  }'
```

### 2. Login and Get JWT Token

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "instructor1",
    "password": "password123"
  }'
```

Save the returned JWT token for authenticated requests.

### 3. Create a Course (Instructor Only)

```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Introduction to Java Programming",
    "description": "Learn the fundamentals of Java programming language",
    "instructorId": 2
  }'
```

### 4. Create an Assignment (Instructor Only)

```bash
curl -X POST http://localhost:8080/api/assignments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Java Fundamentals Quiz",
    "description": "Test your knowledge of Java basics",
    "courseId": 1,
    "dueDate": "2025-08-10T23:59:59",
    "maxScore": 100
  }'
```

## 📊 Key Features Implemented

### ✅ Module-Based Architecture
- **User Management Module**: Registration, authentication, role-based access
- **Course Management Module**: Course CRUD, module sequencing with LinkedList
- **Assignment Management Module**: Assignment creation, submission handling with PriorityQueue
- **Reporting Module**: Analytics and performance tracking with TreeMap

### ✅ Custom Data Structures
- **LinkedList**: Course module sequencing
- **HashMap**: User caching for performance
- **PriorityQueue**: Assignment submission prioritization by deadlines
- **TreeMap**: Time-series data for reporting

### ✅ Security Features
- JWT-based authentication
- Role-based authorization (STUDENT, INSTRUCTOR, ADMIN)
- Password encryption with BCrypt
- Secure API endpoints

### ✅ Database Schema
- Users table with role-based access
- Courses with instructor relationships
- Modules with sequential ordering
- Assignments with due dates and scoring
- Submissions with grading support
- Course enrollments with progress tracking

## 🔧 Configuration Options

### Application Profiles

**Development Profile (`application-dev.yml`)**
- H2 in-memory database
- Debug logging enabled
- H2 console accessible
- Hibernate DDL auto-create

**Production Profile (`application.yml`)**
- MySQL database
- Optimized logging
- Production security settings
- Hibernate DDL update mode

### Environment Variables

You can override configuration using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ocms_db
export SPRING_DATASOURCE_USERNAME=ocms_user
export SPRING_DATASOURCE_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret_key
```

## 🐳 Docker Deployment (Optional)

Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-alpine
COPY target/online-course-management-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Run with Docker:
```bash
mvn clean package -DskipTests
docker build -t ocms .
docker run -p 8080:8080 ocms
```

## 🔍 Monitoring and Health Checks

The application includes Spring Boot Actuator endpoints:

- Health Check: `GET /actuator/health`
- Application Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`

## 🚨 Troubleshooting

### Common Issues

1. **Circular Dependency Error**
   - Fixed by separating PasswordEncoder configuration
   - Ensure proper bean separation

2. **JWT Token Issues**
   - Verify JWT secret length (minimum 512 bits for HS512)
   - Check token expiration settings

3. **Database Connection Issues**
   - Verify database is running
   - Check connection credentials
   - Ensure database exists

4. **Hibernate Proxy Serialization**
   - Jackson Hibernate module configured
   - Lazy loading handled properly

### Logs Location

Application logs are available in the console output. For production, configure logging to files:

```yaml
logging:
  file:
    name: logs/ocms.log
  level:
    com.ocms: INFO
```

## 📈 Performance Considerations

- **Caching**: User data cached in HashMap for quick access
- **Database**: Connection pooling with HikariCP
- **Security**: Stateless JWT authentication
- **Lazy Loading**: Hibernate entities use lazy loading for performance

## 🔐 Security Best Practices

1. Change default JWT secret in production
2. Use HTTPS in production environment
3. Implement rate limiting for API endpoints
4. Regular security updates for dependencies
5. Monitor and log security events

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Verify configuration settings
4. Test with development profile first