# OCMS API Documentation

## Overview

The Online Course Management System (OCMS) provides a comprehensive REST API for managing courses, assignments, users, and generating reports. All endpoints require JWT authentication except for registration and login.

## Base URL
```
http://localhost:8080
```

## Authentication

### Register User
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

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT",
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

### Login
```http
POST /api/users/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "message": "Login successful"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

## User Management

### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer <jwt_token>
```

### Get User by Username
```http
GET /api/users/username/{username}
Authorization: Bearer <jwt_token>
```

### Get All Users (Admin only)
```http
GET /api/users
Authorization: Bearer <jwt_token>
```

### Get Users by Role (Admin only)
```http
GET /api/users/role/{role}
Authorization: Bearer <jwt_token>
```

### Update User
```http
PUT /api/users/{id}
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "firstName": "John Updated",
  "lastName": "Doe Updated",
  "email": "john.updated@example.com"
}
```

### Delete User (Admin only)
```http
DELETE /api/users/{id}
Authorization: Bearer <jwt_token>
```

## Course Management

### Create Course
```http
POST /api/courses
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Introduction to Java Programming",
  "description": "Learn the basics of Java programming language",
  "instructorId": 1
}
```

### Get Course by ID
```http
GET /api/courses/{id}
Authorization: Bearer <jwt_token>
```

### Get All Courses
```http
GET /api/courses
Authorization: Bearer <jwt_token>
```

### Get Courses by Instructor
```http
GET /api/courses/instructor/{instructorId}
Authorization: Bearer <jwt_token>
```

### Get Courses by Student
```http
GET /api/courses/student/{studentId}
Authorization: Bearer <jwt_token>
```

### Update Course
```http
PUT /api/courses/{id}
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Advanced Java Programming",
  "description": "Advanced concepts in Java programming"
}
```

### Delete Course
```http
DELETE /api/courses/{id}
Authorization: Bearer <jwt_token>
```

## Module Management

### Add Module to Course
```http
POST /api/courses/modules
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "moduleTitle": "Java Basics",
  "content": "Introduction to Java syntax and basic concepts",
  "moduleOrder": 1,
  "courseId": 1
}
```

### Get Modules by Course
```http
GET /api/courses/{courseId}/modules
Authorization: Bearer <jwt_token>
```

### Get Module by ID
```http
GET /api/courses/modules/{id}
Authorization: Bearer <jwt_token>
```

### Update Module
```http
PUT /api/courses/modules/{id}
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "moduleTitle": "Updated Java Basics",
  "content": "Updated content for Java basics"
}
```

### Delete Module
```http
DELETE /api/courses/modules/{id}
Authorization: Bearer <jwt_token>
```

### Get Module Sequence (LinkedList)
```http
GET /api/courses/{courseId}/module-sequence
Authorization: Bearer <jwt_token>
```

## Course Enrollment

### Enroll Student in Course
```http
POST /api/courses/{courseId}/enroll/{studentId}
Authorization: Bearer <jwt_token>
```

### Get Enrollments by Course
```http
GET /api/courses/{courseId}/enrollments
Authorization: Bearer <jwt_token>
```

## Assignment Management

### Create Assignment
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

### Get Assignment by ID
```http
GET /api/assignments/{id}
Authorization: Bearer <jwt_token>
```

### Get All Assignments
```http
GET /api/assignments
Authorization: Bearer <jwt_token>
```

### Get Assignments by Course
```http
GET /api/assignments/course/{courseId}
Authorization: Bearer <jwt_token>
```

### Update Assignment
```http
PUT /api/assignments/{id}
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Updated Java Quiz",
  "dueDate": "2024-12-31T23:59:59",
  "maxScore": 150
}
```

### Delete Assignment
```http
DELETE /api/assignments/{id}
Authorization: Bearer <jwt_token>
```

## Assignment Submission

### Submit Assignment (PriorityQueue)
```http
POST /api/assignments/submit
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "assignmentId": 1,
  "userId": 1,
  "content": "My assignment submission content"
}
```

### Get Submissions by Assignment
```http
GET /api/assignments/submissions/assignment/{assignmentId}
Authorization: Bearer <jwt_token>
```

### Get Submissions by User
```http
GET /api/assignments/submissions/user/{userId}
Authorization: Bearer <jwt_token>
```

### Get Submission by ID
```http
GET /api/assignments/submissions/{id}
Authorization: Bearer <jwt_token>
```

### Grade Submission
```http
POST /api/assignments/submissions/{id}/grade
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "score": 85.5,
  "feedback": "Good work! Consider improving error handling."
}
```

### Get Ungraded Submissions
```http
GET /api/assignments/submissions/{assignmentId}/ungraded
Authorization: Bearer <jwt_token>
```

### Get Graded Submissions
```http
GET /api/assignments/submissions/{assignmentId}/graded
Authorization: Bearer <jwt_token>
```

### Get Average Score
```http
GET /api/assignments/submissions/{assignmentId}/average-score
Authorization: Bearer <jwt_token>
```

### Get Overdue Assignments
```http
GET /api/assignments/overdue
Authorization: Bearer <jwt_token>
```

### Get Assignments by Date Range
```http
GET /api/assignments/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
Authorization: Bearer <jwt_token>
```

### Get Next Submission to Process (PriorityQueue)
```http
GET /api/assignments/submissions/next-to-process
Authorization: Bearer <jwt_token>
```

### Get Assignment Statistics
```http
GET /api/assignments/{assignmentId}/statistics
Authorization: Bearer <jwt_token>
```

## Reporting

### Generate Custom Report (TreeMap)
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

### Generate Student Performance Report
```http
GET /api/reports/student-performance/{studentId}?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
Authorization: Bearer <jwt_token>
```

### Generate Course Completion Report
```http
GET /api/reports/course-completion/{courseId}
Authorization: Bearer <jwt_token>
```

### Generate Assignment Statistics Report
```http
GET /api/reports/assignment-statistics/{assignmentId}
Authorization: Bearer <jwt_token>
```

### Generate System Activity Report
```http
GET /api/reports/system-activity?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
Authorization: Bearer <jwt_token>
```

### Generate User Activity Report
```http
GET /api/reports/user-activity/{userId}?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
Authorization: Bearer <jwt_token>
```

### Generate Instructor Performance Report
```http
GET /api/reports/instructor-performance/{instructorId}?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
Authorization: Bearer <jwt_token>
```

### Get Activity Log (TreeMap)
```http
GET /api/reports/activity-log
Authorization: Bearer <jwt_token>
```

### Log Activity
```http
POST /api/reports/log-activity?activity=User logged in
Authorization: Bearer <jwt_token>
```

## Data Structure Endpoints

### Get User Cache (HashMap)
```http
GET /api/users/cache
Authorization: Bearer <jwt_token>
```

### Clear User Cache
```http
DELETE /api/users/cache
Authorization: Bearer <jwt_token>
```

### Get Course Module Sequences (LinkedList)
```http
GET /api/courses/module-sequences
Authorization: Bearer <jwt_token>
```

### Get Course Enrollment Cache (HashMap)
```http
GET /api/courses/enrollment-cache
Authorization: Bearer <jwt_token>
```

## Error Responses

All endpoints return consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-01T10:00:00"
}
```

Common HTTP status codes:
- `200 OK`: Success
- `201 Created`: Resource created
- `400 Bad Request`: Invalid input
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Rate Limiting

The API implements rate limiting to prevent abuse. Limits are applied per user and endpoint.

## CORS

CORS is enabled for cross-origin requests. Configure allowed origins in the security configuration if needed.