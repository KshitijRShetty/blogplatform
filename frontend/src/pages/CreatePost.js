import React, { useState } from 'react';
import './CreatePost.css';

const CreatePost = ({ onSubmit }) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSubmit) onSubmit({ title, content });
    setTitle('');
    setContent('');
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
    </div>
  );
};

export default CreatePost;