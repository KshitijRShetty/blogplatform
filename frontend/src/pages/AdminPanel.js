import React, { useState, useEffect } from 'react';
import { blogAPI } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import './AdminPanel.css';

const AdminPanel = () => {
  const [pendingPosts, setPendingPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(null);
  const { hasRole } = useAuth();

  useEffect(() => {
    if (hasRole && hasRole('EDITOR')) {
      fetchPendingPosts();
    }
  }, [hasRole]);

  const fetchPendingPosts = async () => {
    try {
      setLoading(true);
      const response = await blogAPI.getPendingPosts();
      const posts = response.data;
  
      console.log('Raw API response:', posts);
  
      if (Array.isArray(posts)) {
        // Backend now returns only PENDING posts in correct format
        console.log('Setting posts:', posts);
        setPendingPosts(posts);
      } else {
        setPendingPosts([]);
      }
    } catch (error) {
      console.error('Error fetching pending posts:', error);
      setPendingPosts([]);
    } finally {
      setLoading(false);
    }
  };
  

  const handleApprove = async (postId) => {
    setActionLoading(postId);
    try {
      await blogAPI.approvePost(postId);
      fetchPendingPosts();
      alert('Post approved successfully!');
    } catch (error) {
      console.error('Error approving post:', error);
      alert('Failed to approve post');
    } finally {
      setActionLoading(null);
    }
  };

  const handleReject = async (postId) => {
    if (window.confirm('Are you sure you want to reject this post?')) {
      setActionLoading(postId);
      try {
        await blogAPI.rejectPost(postId);
        fetchPendingPosts();
        alert('Post rejected successfully!');
      } catch (error) {
        console.error('Error rejecting post:', error);
        alert('Failed to reject post');
      } finally {
        setActionLoading(null);
      }
    }
  };

  if (!hasRole || !hasRole('EDITOR')) {
    return (
      <div className="admin-panel">
        <div className="error">
          You don't have permission to access the admin panel.
        </div>
      </div>
    );
  }

  console.log('pendingPosts state during render:', pendingPosts);

  return (
    <div className="admin-panel">
      <h1>Admin Panel</h1>
      <p>Manage pending blog posts</p>

      {loading ? (
        <div className="loading">Loading pending posts...</div>
      ) : (
        <div className="pending-posts">
          <h2>Pending Posts ({Array.isArray(pendingPosts) ? pendingPosts.length : 0})</h2>

          {Array.isArray(pendingPosts) && pendingPosts.length === 0 ? (
            <p className="no-posts">No posts pending approval.</p>
          ) : (
            <div className="posts-list">
              {pendingPosts.map((post) => (
                <div key={post.id} className="post-item">
                  <div className="post-header">
                    <h3>{post.title}</h3>
                    <div className="post-author">
                      By {post.author?.username || 'Unknown'}
                    </div>
                    <div className="post-date">
                      {new Date(post.creationDate).toLocaleDateString()}
                    </div>
                  </div>

                  <div className="post-content-preview">
                    {post.content.length > 200
                      ? `${post.content.substring(0, 200)}...`
                      : post.content}
                  </div>

                  <div className="post-actions">
                    <button
                      onClick={() => handleApprove(post.id)}
                      disabled={actionLoading === post.id}
                      className="approve-btn"
                    >
                      {actionLoading === post.id ? 'Processing...' : 'Approve'}
                    </button>

                    <button
                      onClick={() => handleReject(post.id)}
                      disabled={actionLoading === post.id}
                      className="reject-btn"
                    >
                      {actionLoading === post.id ? 'Processing...' : 'Reject'}
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default AdminPanel;