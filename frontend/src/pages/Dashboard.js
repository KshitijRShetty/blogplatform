import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import './Dashboard.css';

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <div className="dashboard-container">
      <h1>Dashboard</h1>
      <div className="welcome">
        <span>Welcome back, <strong>{user?.username || "User"}</strong>!</span>
      </div>
      <div className="roles-section">
        <h2>Your Roles:</h2>
        <ul>
          {(user?.roles || []).map(role => (
            <li key={role} className="role">{role}</li>
          ))}
        </ul>
      </div>
      <div className="actions-section">
        <h2>Reader Actions</h2>
        <ul>
          <li>Browse posts and leave comments</li>
        </ul>
      </div>
    </div>
  );
};

export default Dashboard;