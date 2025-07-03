import React, { useState } from 'react';
import { blogAPI } from '../services/api';
import './CreatePost.css';

const CreatePost = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Step 1: Create the post (status will be DRAFT initially)
      const response = await blogAPI.createPost({ title, content });
      const postId = response.data.id;

     
      await blogAPI.submitForReview(postId);

      setMessage(`Post created and submitted for review! ID: ${postId}`);
      setTitle('');
      setContent('');
    } catch (error) {
      console.error('Error creating/submitting post:', error);
      setMessage('Error creating or submitting post.');
    }
  };

  return (
    <div className="create-post-container">
      <h2>Create New Post</h2>
      <form className="create-post-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            type="text"
            value={title}
            onChange={e => setTitle(e.target.value)}
            placeholder="Enter post title"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="content">Content</label>
          <textarea
            id="content"
            value={content}
            onChange={e => setContent(e.target.value)}
            placeholder="Write your post here..."
            rows={6}
            required
          />
        </div>
        <button type="submit" className="submit-btn">Publish</button>
      </form>
      {message && <div className="message">{message}</div>}
    </div>
  );
};

export default CreatePost;
