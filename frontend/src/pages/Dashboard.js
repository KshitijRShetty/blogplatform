import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Dashboard.css';

const Dashboard = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 17) return 'Good afternoon';
    return 'Good evening';
  };

  // Check if user has author or admin role (can create posts)
  const canCreatePosts = user?.roles?.some(role => 
    role.toLowerCase() === 'author' || role.toLowerCase() === 'admin'
  );

  const quickActions = [
    { title: 'Browse Posts', description: 'Discover new content', icon: '📚', onClick: () => navigate('/') },
    { title: 'Search', description: 'Find specific content', icon: '🔍', onClick: () => navigate('/search') },
    ...(canCreatePosts ? [{ title: 'Create Post', description: 'Share your thoughts', icon: '✍️', onClick: () => navigate('/create-post') }] : [])
  ];

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <div className="welcome-section">
          <h1 className="dashboard-title">Welcome back!</h1>
          <p className="greeting">
            {getGreeting()}, <strong>{user?.username || "User"}</strong> 👋
          </p>
        </div>
      </div>

      <div className="actions-section">
        <h2 className="section-title">Quick Actions</h2>
        <div className="actions-grid">
          {quickActions.map((action, index) => (
            <div key={index} className="action-card" onClick={action.onClick}>
              <div className="action-icon">{action.icon}</div>
              <div className="action-content">
                <h3 className="action-title">{action.title}</h3>
                <p className="action-description">{action.description}</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="account-section">
        <h2 className="section-title">Account Info</h2>
        <div className="account-info">
          <div className="user-avatar">
            <div className="avatar-circle">
              {(user?.username || "U").charAt(0).toUpperCase()}
            </div>
          </div>
          <div className="user-details">
            <p className="username">{user?.username || "User"}</p>
            <p className="user-email">{user?.email || "No email available"}</p>
            {user?.roles && user.roles.length > 0 && (
              <div className="user-roles">
                {user.roles.map(role => (
                  <span key={role} className="role-badge">{role}</span>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
