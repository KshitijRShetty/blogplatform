import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle token expiration
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API calls
export const authAPI = {
  register: (userData) => api.post('/auth/register', userData),
  login: (credentials) => api.post('/auth/login', credentials),
  validateToken: () => api.get('/auth/validate'),
};

// Blog posts API calls
export const blogAPI = {
  getAllPosts: () => api.get('/blogposts/public/all'),
  getAllPostsWithLikes: () => api.get('/blogposts/all'),
  getPost: (postId) => api.get(`/blogposts/public/${postId}`),
  getPostWithLikes: (postId) => api.get(`/blogposts/${postId}`),
  createPost: (postData) => api.post('/blogposts', postData),
  updatePost: (postId, postData) => api.put(`/blogposts/${postId}`, postData),
  deletePost: (postId) => api.delete(`/blogposts/${postId}`),
  approvePost: (postId) => api.put(`/blogposts/${postId}/approve`),
  rejectPost: (postId) => api.put(`/blogposts/${postId}/reject`),
  submitForReview: (postId) => api.put(`/blogposts/${postId}/submit`),
  getMyPosts: () => api.get('/blogposts/my'),
  getPendingPosts: () => api.get('/blogposts/pending'),
};

// Comments API calls
export const commentAPI = {
  getComments: (postId) => api.get(`/comments/post/${postId}`),
  createComment: (commentData) => api.post('/comments', commentData),
  updateComment: (commentId, commentData) => api.put(`/comments/${commentId}`, commentData),
  deleteComment: (commentId) => api.delete(`/comments/${commentId}`),
};

// Follow API calls
export const followAPI = {
  followUser: (userId) => api.post(`/follow/${userId}`),
  unfollowUser: (userId) => api.delete(`/follow/${userId}`),
  getFollowing: () => api.get('/follow/following'),
  getFollowers: () => api.get('/follow/followers'),
};

// Search API calls
export const searchAPI = {
  // Enhanced search API call
  searchPosts: (query) => api.get(`/search/posts?q=${encodeURIComponent(query)}`),
  searchPostsByAuthor: (username) => api.get(`/search/posts/by-author?username=${encodeURIComponent(username)}`),
};

// Like API calls
export const likeAPI = {
  toggleLike: (postId) => api.post(`/likes/toggle/${postId}`),
  getLikeStatus: (postId) => api.get(`/likes/status/${postId}`),
  getLikeCount: (postId) => api.get(`/likes/count/${postId}`),
};

export default api;
