# Blog Platform API

A complete REST API for a multi-role blog platform built with Spring Boot and PostgreSQL.

## Features

- **Authentication**: JWT-based registration/login
- **Role-based Access**: Blogger, Reader, Editor roles
- **Content Management**: Post lifecycle (Draft → Pending → Approved)
- **Social Features**: Comments, Follow/Unfollow users
- **Search**: Basic post and user search functionality
- **Security**: Role-based endpoint protection, input validation

## Quick Start

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Maven 3.6+

### Setup
1. Create PostgreSQL database:
```sql
CREATE DATABASE blog_platform;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_platform
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run the application:
```bash
mvn spring-boot:run
```

### Test Users
The application initializes with test users:
- **Editor**: username=`editor`, password=`password123`
- **Blogger**: username=`blogger`, password=`password123` 
- **Reader**: username=`reader`, password=`password123`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/validate` - Validate JWT token

### Blog Posts
- `GET /api/blogposts/public/all` - Get all approved posts (public)
- `GET /api/blogposts/public/{id}` - Get specific post (public)
- `POST /api/blogposts` - Create new post (Blogger/Editor)
- `PUT /api/blogposts/{id}` - Update post (Author only)
- `DELETE /api/blogposts/{id}` - Delete post (Author only)
- `GET /api/blogposts/my` - Get current user's posts
- `GET /api/blogposts/pending` - Get pending posts (Editor only)
- `PUT /api/blogposts/{id}/approve` - Approve post (Editor only)
- `PUT /api/blogposts/{id}/reject` - Reject post (Editor only)
- `PUT /api/blogposts/{id}/submit` - Submit for review (Author only)

### Comments
- `POST /api/comments` - Create comment
- `PUT /api/comments/{id}` - Update comment (Author only)
- `DELETE /api/comments/{id}` - Delete comment (Author only)
- `GET /api/comments/post/{postId}` - Get comments for post
- `GET /api/comments/user/{userId}` - Get user's comments
- `GET /api/comments/{id}` - Get specific comment

### Follow System
- `POST /api/follow/{userId}` - Follow user
- `DELETE /api/follow/{userId}` - Unfollow user
- `GET /api/follow/following` - Get users you follow
- `GET /api/follow/followers` - Get your followers
- `GET /api/follow/following/{userId}` - Get user's following list
- `GET /api/follow/followers/{userId}` - Get user's followers
- `GET /api/follow/check/{userId}` - Check if following user
- `GET /api/follow/stats/{userId}` - Get follow statistics

### Search
- `GET /api/search/posts?q=query` - Search posts
- `GET /api/search/users?q=query` - Search users
- `GET /api/search/all?q=query` - Search posts and users

## Request/Response Examples

### Register User
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "roles": ["BLOGGER"]
}
```

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "blogger",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "blogger",
  "email": "blogger@example.com",
  "roles": ["BLOGGER"]
}
```

### Create Blog Post
```bash
POST /api/blogposts
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "title": "My First Post",
  "content": "This is the content of my first blog post.",
  "tags": ["introduction", "first-post"]
}
```

### Create Comment
```bash
POST /api/comments
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "blogPostId": 1,
  "content": "Great post! Thanks for sharing."
}
```

### Follow User
```bash
POST /api/follow/2
Authorization: Bearer your_jwt_token
```

## User Roles

### Reader
- View approved blog posts
- Comment on posts
- Follow/unfollow users
- Search content

### Blogger
- All Reader permissions
- Create, edit, delete own posts
- Submit posts for review

### Editor
- All Blogger permissions
- Approve/reject any posts
- View pending posts
- Moderate content

## Post Lifecycle

1. **Draft** - Initially created by blogger
2. **Pending** - Submitted for editor review
3. **Approved** - Published and visible to all
4. **Rejected** - Declined by editor

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- `400 Bad Request` - Validation errors
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server errors

## Security

- JWT token authentication
- Role-based access control
- Input validation
- Password hashing (BCrypt)
- CORS enabled for development

## Database Schema

The application uses PostgreSQL with the following main tables:
- `users` - User accounts and roles
- `blog_post` - Blog posts with status
- `comments` - Post comments
- `follows` - User follow relationships
- `user_roles` - User role mapping
- `blog_post_tags` - Post tags

## Development Notes

- Uses Spring Boot 3.x with Java 17
- Hibernate for ORM with automatic schema generation
- Lombok for reducing boilerplate code
- JWT for stateless authentication
- Global exception handling
- Validation with Bean Validation API
