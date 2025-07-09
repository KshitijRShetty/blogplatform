import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { blogAPI } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import LikeButton from '../components/LikeButton';
import './Home.css';

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    fetchPosts();
  }, [user]);

  const fetchPosts = async () => {
    try {
      setLoading(true);
      // Use authenticated endpoint if user is logged in, otherwise use public endpoint
      const response = user ? await blogAPI.getAllPostsWithLikes() : await blogAPI.getAllPosts();
      setPosts(response.data);
    } catch (error) {
      setError('Failed to load posts');
      console.error('Error fetching posts:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading posts...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <div className="home">
      <div className="hero">
        <h1>Welcome to BlogPlatform</h1>
        <p>Discover amazing stories from our community of writers</p>
        <p className="search-tip">
           <strong>New!</strong> Use our enhanced search to find posts by author name.
        </p>
      </div>

      <div className="posts-container">
        <h2>Latest Posts</h2>
        {posts.length === 0 ? (
          <p className="no-posts">No posts available yet.</p>
        ) : (
          <div className="posts-grid">
            {posts.map((post) => (
              <div key={post.id} className="post-card">
                <h3>
                  <Link to={`/post/${post.id}`}>{post.title}</Link>
                </h3>
                <p className="post-content">
                  {post.content.length > 150 
                    ? `${post.content.substring(0, 150)}...` 
                    : post.content
                  }
                </p>
                <div className="post-meta">
                  <span className="post-author">By {post.author?.username}</span>
                  <span className="post-date">
                    {new Date(post.creationDate).toLocaleDateString()}
                  </span>
                </div>
                <div className="post-status">
                  <span className={`status ${post.status.toLowerCase()}`}>
                    {post.status}
                  </span>
                </div>
                <LikeButton 
                  postId={post.id} 
                  initialLikeCount={post.likeCount} 
                  initialIsLiked={post.isLikedByCurrentUser} 
                  onLikeUpdate={(updatedPostId, newIsLiked, newLikeCount) => {
                    setPosts(prevPosts => prevPosts.map(p =>
                      p.id === updatedPostId 
                      ? { ...p, isLikedByCurrentUser: newIsLiked, likeCount: newLikeCount } 
                      : p));
                  }}
                />
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
