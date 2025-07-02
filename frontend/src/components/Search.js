import React, { useState } from 'react';
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
            placeholder="Search posts..."
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
                <h4>{post.title}</h4>
                <p>{post.content.substring(0, 150)}...</p>
                <div className="result-meta">
                  <span>By {post.author?.username}</span>
                  <span>{new Date(post.creationDate).toLocaleDateString()}</span>
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
