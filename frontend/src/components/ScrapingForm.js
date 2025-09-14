import React, { useState, useEffect } from 'react';
import { getWebsites, createScrapingJob } from '../utils/api';

function ScrapingForm({ onJobCreated }) {
  const [websites, setWebsites] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    website: '',
    searchQuery: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadWebsites();
  }, []);

  const loadWebsites = async () => {
    try {
      const response = await getWebsites();
      setWebsites(response.data.websites);
    } catch (error) {
      setError('Failed to load websites');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await createScrapingJob(formData);
      onJobCreated(response.data);
      setFormData({ name: '', website: '', searchQuery: '' });
    } catch (error) {
      setError(error.response?.data?.message || 'Failed to create job');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="px-4 py-6 sm:px-0">
      <div className="border-4 border-dashed border-gray-200 rounded-lg p-6">
        <h2 className="text-2xl font-bold text-gray-900 mb-6">Create New Scraping Job</h2>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700">Job Name</label>
            <input
              type="text"
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              value={formData.name}
              onChange={(e) => setFormData({...formData, name: e.target.value})}
              placeholder="Enter job name"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Website</label>
            <select
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              value={formData.website}
              onChange={(e) => setFormData({...formData, website: e.target.value})}
            >
              <option value="">Select a website</option>
              {websites.map((website) => (
                <option key={website} value={website}>{website}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Search Query</label>
            <input
              type="text"
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              value={formData.searchQuery}
              onChange={(e) => setFormData({...formData, searchQuery: e.target.value})}
              placeholder="Enter search terms"
            />
          </div>

          {error && (
            <div className="text-red-600 text-sm">{error}</div>
          )}

          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
            >
              {loading ? 'Creating Job...' : 'Start Scraping'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ScrapingForm;