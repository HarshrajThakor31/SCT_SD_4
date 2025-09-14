import React, { useState } from 'react';
import { login, register } from '../utils/api';

function Login({ onLogin }) {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isLogin) {
        const response = await login(formData.username, formData.password);
        localStorage.setItem('token', response.data.token);
        onLogin();
      } else {
        await register(formData.username, formData.email, formData.password);
        setIsLogin(true);
        setError('Registration successful! Please login.');
      }
    } catch (error) {
      setError(error.response?.data?.message || 'Authentication failed');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            {isLogin ? 'Sign in' : 'Sign up'}
          </h2>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md shadow-sm -space-y-px">
            <input
              type="text"
              required
              className="relative block w-full px-3 py-2 border border-gray-300 rounded-t-md placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Username"
              value={formData.username}
              onChange={(e) => setFormData({...formData, username: e.target.value})}
            />
            {!isLogin && (
              <input
                type="email"
                required
                className="relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="Email"
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
              />
            )}
            <input
              type="password"
              required
              className="relative block w-full px-3 py-2 border border-gray-300 rounded-b-md placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Password"
              value={formData.password}
              onChange={(e) => setFormData({...formData, password: e.target.value})}
            />
          </div>

          {error && (
            <div className="text-red-600 text-sm text-center">{error}</div>
          )}

          <div>
            <button
              type="submit"
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              {isLogin ? 'Sign in' : 'Sign up'}
            </button>
          </div>

          <div className="text-center space-y-2">
            <button
              type="button"
              className="text-indigo-600 hover:text-indigo-500"
              onClick={() => setIsLogin(!isLogin)}
            >
              {isLogin ? 'Need an account? Sign up' : 'Have an account? Sign in'}
            </button>
            
            {isLogin && (
              <div className="bg-blue-50 p-3 rounded-md">
                <p className="text-sm text-blue-800 font-medium">Default Login:</p>
                <p className="text-xs text-blue-600">Username: admin</p>
                <p className="text-xs text-blue-600">Password: admin123</p>
                <button
                  type="button"
                  className="mt-1 text-xs text-blue-700 underline"
                  onClick={() => setFormData({...formData, username: 'admin', password: 'admin123'})}
                >
                  Use default credentials
                </button>
              </div>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;