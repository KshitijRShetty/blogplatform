import React, { useState } from 'react';
import { blogAPI } from '../services/api'; // <-- import your API
import './CreatePost.css';

const CreatePost = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await blogAPI.createPost({ title, content });
      setMessage('Post created! ID: ' + response.data.id);
      setTitle('');
      setContent('');
    } catch (error) {
      setMessage('Error creating post.');
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