import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const login = (username, password) => {
  return api.post('/auth/login', { username, password });
};

export const register = (username, email, password) => {
  return api.post('/auth/register', { username, email, password });
};

export const getWebsites = () => {
  return api.get('/websites');
};

export const createScrapingJob = (jobData) => {
  return api.post('/scraping-jobs', jobData);
};

export const getScrapingJobs = () => {
  return api.get('/scraping-jobs');
};

export const getProducts = (jobId = null) => {
  const params = jobId ? { jobId } : {};
  return api.get('/products', { params });
};

export const exportData = (format, jobId = null) => {
  const params = jobId ? { jobId } : {};
  return api.get(`/export/${format}`, { 
    params,
    responseType: 'blob'
  });
};

export const getDashboardStats = () => {
  return api.get('/dashboard/stats');
};

export default api;