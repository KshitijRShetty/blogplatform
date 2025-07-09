import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { blogAPI, commentAPI } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import LikeButton from './LikeButton';
import './PostDetail.css';

const PostDetail = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [loading, setLoading] = useState(true);
  const [commentLoading, setCommentLoading] = useState(false);
  const { isAuthenticated, user } = useAuth();

  const fetchPost = useCallback(async () => {
    try {
      // Use authenticated endpoint if user is logged in, otherwise use public endpoint
      const response = user ? await blogAPI.getPostWithLikes(postId) : await blogAPI.getPost(postId);
      setPost(response.data);
    } catch (error) {
      console.error('Error fetching post:', error);
    }
  }, [postId, user]);

  const fetchComments = useCallback(async () => {
    try {
      const response = await commentAPI.getComments(postId);
      setComments(response.data);
    } catch (error) {
      console.error('Error fetching comments:', error);
    } finally {
      setLoading(false);
    }
  }, [postId]);

  useEffect(() => {
    fetchPost();
    fetchComments();
  }, [fetchPost, fetchComments]);

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    setCommentLoading(true);
    try {
      const response = await commentAPI.createComment({
        blogPostId: parseInt(postId),
        content: newComment
      });
      setNewComment('');
      // Add the new comment to the top of the list
      setComments(prevComments => [response.data, ...prevComments]);
    } catch (error) {
      console.error('Error creating comment:', error);
      alert('Failed to post comment. Please try again.');
    } finally {
      setCommentLoading(false);
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (window.confirm('Are you sure you want to delete this comment?')) {
      try {
        await commentAPI.deleteComment(commentId);
        // Remove the comment from the list immediately
        setComments(prevComments => prevComments.filter(comment => comment.id !== commentId));
      } catch (error) {
        console.error('Error deleting comment:', error);
        alert('Failed to delete comment. Please try again.');
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading post...</div>;
  }

  if (!post) {
    return <div className="error">Post not found</div>;
  }

  return (
    <div className="post-detail">
      <article className="post-content">
        <h1>{post.title}</h1>
        <div className="post-meta">
          <span>By {post.author?.username}</span>
          <span>{new Date(post.creationDate).toLocaleDateString()}</span>
          <span className={`status ${post.status.toLowerCase()}`}>
            {post.status}
          </span>
        </div>
        <div className="post-body">
          {post.content.split('\n').map((paragraph, index) => (
            <p key={index}>{paragraph}</p>
          ))}
        </div>
        <LikeButton 
          postId={post.id} 
          initialLikeCount={post.likeCount || 0} 
          initialIsLiked={post.isLikedByCurrentUser || false} 
          onLikeUpdate={(updatedPostId, newIsLiked, newLikeCount) => {
            setPost(prevPost => ({ 
              ...prevPost, 
              isLikedByCurrentUser: newIsLiked, 
              likeCount: newLikeCount 
            }));
          }}
        />
      </article>

      <section className="comments-section">
        <h3>Comments ({comments.length})</h3>
        
        {isAuthenticated() && (
          <form onSubmit={handleCommentSubmit} className="comment-form">
            <textarea
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              placeholder="Write a comment..."
              rows="3"
              required
            />
            <button type="submit" disabled={commentLoading}>
              {commentLoading ? 'Posting...' : 'Post Comment'}
            </button>
          </form>
        )}

        <div className="comments-list">
          {comments.map((comment) => (
            <div key={comment.id} className="comment">
              <div className="comment-header">
                <strong>{comment.user?.username}</strong>
                <span className="comment-date">
                  {new Date(comment.createdAt).toLocaleDateString()}
                </span>
                {user?.id === comment.user?.id && (
                  <button 
                    onClick={() => handleDeleteComment(comment.id)}
                    className="delete-comment"
                  >
                    Delete
                  </button>
                )}
              </div>
              <div className="comment-content">
                {comment.content}
              </div>
            </div>
          ))}
        </div>

        {comments.length === 0 && (
          <p className="no-comments">No comments yet. Be the first to comment!</p>
        )}
      </section>
    </div>
  );
};

export default PostDetail;
