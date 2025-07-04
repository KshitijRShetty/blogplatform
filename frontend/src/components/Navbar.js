import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, isAuthenticated, logout, hasRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          BlogPlatform
        </Link>
        
        <div className="nav-menu">
          <Link to="/" className="nav-link">
            Home
          </Link>
          
          <Link to="/search" className="nav-link" title="Search posts by title, content, or author">
             Search
          </Link>
          
          {isAuthenticated() ? (
            <>
              <Link to="/dashboard" className="nav-link">
                Dashboard
              </Link>
              
              {(hasRole('BLOGGER') || hasRole('EDITOR')) && (
                <Link to="/create-post" className="nav-link">
                  Create Post
                </Link>
              )}
              
              {hasRole('EDITOR') && (
                <Link to="/admin" className="nav-link">
                  Admin
                </Link>
              )}
              
              <div className="nav-user">
                <span className="nav-username">Welcome, {user?.username}</span>
                <button onClick={handleLogout} className="nav-logout">
                  Logout
                </button>
              </div>
            </>
          ) : (
            <div className="nav-auth">
              <Link to="/login" className="nav-link">
                Login
              </Link>
              <Link to="/register" className="nav-link nav-register">
                Register
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
