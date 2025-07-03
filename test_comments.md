# Comment System Test Plan

## Backend Changes Made:
1. ✅ Fixed Comment model with proper JSON annotations
2. ✅ Added CommentResponse DTO to avoid circular references
3. ✅ Updated CommentRepository with proper ordering (newest first)
4. ✅ Enhanced CommentService with DTO methods
5. ✅ Updated CommentController to return CommentResponse
6. ✅ Fixed BlogPost and User relationships

## Frontend Changes Made:
1. ✅ Fixed field name mapping (commentId → id)
2. ✅ Improved comment submission to add new comments immediately
3. ✅ Enhanced comment deletion with immediate UI updates
4. ✅ Added better error handling

## To Test:
1. Start the backend server: `mvn spring-boot:run`
2. Start the frontend: `npm start`
3. Login to the application
4. Navigate to any blog post
5. Add a comment - it should appear immediately at the top
6. Delete a comment (if you own it) - it should disappear immediately
7. Refresh the page - comments should persist and be in correct order

## API Endpoints:
- POST /api/comments - Create comment (returns CommentResponse)
- GET /api/comments/post/{postId} - Get comments for a post (returns List<CommentResponse>)
- DELETE /api/comments/{commentId} - Delete comment

## Expected Behavior:
- Comments appear immediately after posting
- Comments are ordered newest first
- User information is properly serialized
- No circular reference errors
- Real-time UI updates without page refreshes
