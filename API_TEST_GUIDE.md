# Blog Platform API Testing Guide

## Prerequisites
1. Make sure PostgreSQL is running on localhost:5432
2. Database 'blog_platform' should exist
3. Spring Boot application should be running on port 8080

## Starting the Application
```bash
mvn spring-boot:run
```

## API Endpoints

### 1. User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "roles": ["BLOGGER"]
  }'
```

### 2. User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Save the JWT token from the response for authenticated requests.

### 3. Create a Blog Post (Requires Authentication)
```bash
curl -X POST http://localhost:8080/api/blogposts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "My First Blog Post",
    "content": "This is the content of my first blog post."
  }'
```

### 4. Get All Posts (Public)
```bash
curl -X GET http://localhost:8080/api/blogposts/public/all
```

### 5. Validate Token
```bash
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Testing with Postman

1. **Import the following collection:**
   - Set Base URL: `http://localhost:8080`
   - Create requests for each endpoint above
   - Use environment variables for JWT tokens

2. **Testing Flow:**
   1. Register a new user
   2. Login with credentials
   3. Copy JWT token
   4. Use token for authenticated requests

## Common Response Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## Database Setup

Make sure PostgreSQL is running and create the database:
```sql
CREATE DATABASE blog_platform;
```

The application will automatically create tables on startup.
