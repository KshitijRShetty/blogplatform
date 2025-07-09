import React, { useState } from 'react';
import { likeAPI } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import './LikeButton.css';

const LikeButton = ({ postId, initialLikeCount = 0, initialIsLiked = false, onLikeUpdate }) => {
  const [likeCount, setLikeCount] = useState(initialLikeCount);
  const [isLiked, setIsLiked] = useState(initialIsLiked);
  const [isLoading, setIsLoading] = useState(false);
  const { user } = useAuth();

  const handleToggleLike = async () => {
    if (!user) {
      alert('Please login to like posts');
      return;
    }

    setIsLoading(true);
    try {
      const response = await likeAPI.toggleLike(postId);
      const { isLiked: newIsLiked, likeCount: newLikeCount } = response.data;
      
      setIsLiked(newIsLiked);
      setLikeCount(newLikeCount);
      
      if (onLikeUpdate) {
        onLikeUpdate(postId, newIsLiked, newLikeCount);
      }
    } catch (error) {
      console.error('Error toggling like:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="like-button-container">
      <button
        className={`like-button ${isLiked ? 'liked' : ''}`}
        onClick={handleToggleLike}
        disabled={isLoading}
      >
        <span className="like-icon">
          {isLiked ? '❤️' : '🤍'}
        </span>
        <span className="like-count">{likeCount}</span>
      </button>
    </div>
  );
};

export default LikeButton;
