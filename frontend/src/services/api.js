import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || '/api/sudoku';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const sudokuAPI = {
  solvePuzzle: async (grid, algorithm = 'BACKTRACKING', visualize = false) => {
    const response = await api.post('/solve', {
      grid,
      algorithm,
      visualize
    });
    return response.data;
  },

  generatePuzzle: async (difficulty = 'MEDIUM') => {
    const response = await api.get('/generate', {
      params: { difficulty }
    });
    return response.data;
  },

  validatePuzzle: async (grid) => {
    const response = await api.get('/validate', {
      params: { grid }
    });
    return response.data;
  },

  getStatistics: async () => {
    const response = await api.get('/statistics');
    return response.data;
  },

  getPuzzleHistory: async (limit = 10) => {
    const response = await api.get('/history', {
      params: { limit }
    });
    return response.data;
  }
};

export default api;