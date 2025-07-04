import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { searchAPI } from '../services/api';
import './Search.css';

const Search = ({ onResults }) => {
  const [query, setQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState({ posts: [], users: [] });

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    try {
      const response = await searchAPI.searchPosts(query);
      const searchResults = { posts: response.data, users: [] };
      setResults(searchResults);
      if (onResults) {
        onResults(searchResults);
      }
    } catch (error) {
      console.error('Search failed:', error);
      setResults({ posts: [], users: [] });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="search-container">
      <form onSubmit={handleSearch} className="search-form">
        <div className="search-input-group">
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search posts by title, content, or author name..."
            className="search-input"
          />
          <button type="submit" disabled={loading} className="search-button">
            {loading ? 'Searching...' : 'Search'}
          </button>
        </div>
      </form>

      {results.posts.length > 0 && (
        <div className="search-results">
          <h3>Posts ({results.posts.length})</h3>
          <div className="search-results-list">
            {results.posts.map((post) => (
              <div key={post.id} className="search-result-item">
                <h4>
                  <Link to={`/post/${post.id}`} className="result-title-link">
                    {post.title}
                  </Link>
                </h4>
                <p className="result-content">
                  {post.content.length > 150 
                    ? `${post.content.substring(0, 150)}...` 
                    : post.content
                  }
                </p>
                <div className="result-meta">
                  <span className="result-author">By {post.author?.username}</span>
                  <span className="result-date">{new Date(post.creationDate).toLocaleDateString()}</span>
                  <span className={`result-status ${post.status?.toLowerCase()}`}>{post.status}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default Search;
