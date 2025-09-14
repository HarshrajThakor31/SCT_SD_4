import React, { useState, useEffect } from 'react';
import './Statistics.css';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { TrendingUp, Clock, Target, Award } from 'lucide-react';
import { sudokuAPI } from '../services/api';

const Statistics = () => {
  const [stats, setStats] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStatistics();
    loadHistory();
  }, []);

  const loadStatistics = async () => {
    try {
      const data = await sudokuAPI.getStatistics();
      setStats(data);
    } catch (error) {
      console.error('Error loading statistics:', error);
    }
  };

  const loadHistory = async () => {
    try {
      const data = await sudokuAPI.getPuzzleHistory(20);
      setHistory(data);
    } catch (error) {
      console.error('Error loading history:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  const difficultyData = stats?.puzzlesByDifficulty ? 
    Object.entries(stats.puzzlesByDifficulty).map(([difficulty, count]) => ({
      name: difficulty,
      value: count
    })) : [];

  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

  const formatTime = (ms) => {
    if (ms < 1000) return `${ms}ms`;
    if (ms < 60000) return `${(ms / 1000).toFixed(1)}s`;
    return `${(ms / 60000).toFixed(1)}m`;
  };

  return (
    <div className="statistics-container">
      <h2>Statistics Dashboard</h2>
      
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon">
              <Target size={24} />
            </div>
            <div className="stat-value">{stats.totalPuzzles}</div>
            <div className="stat-label">Total Puzzles</div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">
              <Award size={24} />
            </div>
            <div className="stat-value">{stats.solvedPuzzles}</div>
            <div className="stat-label">Solved Puzzles</div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">
              <TrendingUp size={24} />
            </div>
            <div className="stat-value">{stats.successRate.toFixed(1)}%</div>
            <div className="stat-label">Success Rate</div>
          </div>

          <div className="stat-card">
            <div className="stat-icon">
              <Clock size={24} />
            </div>
            <div className="stat-value">{formatTime(stats.averageSolveTimeMs)}</div>
            <div className="stat-label">Avg Solve Time</div>
          </div>
        </div>
      )}

      <div className="charts-container">
        <div className="chart-section">
          <h3>Puzzles by Difficulty</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={difficultyData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {difficultyData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        <div className="chart-section">
          <h3>Difficulty Distribution</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={difficultyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="value" fill="#667eea" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="history-section">
        <h3>Recent Puzzles</h3>
        <div className="history-table">
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Difficulty</th>
                <th>Status</th>
                <th>Solve Time</th>
              </tr>
            </thead>
            <tbody>
              {history.map((puzzle, index) => (
                <tr key={puzzle.id || index}>
                  <td>{new Date(puzzle.createdAt).toLocaleDateString()}</td>
                  <td>
                    <span className={`difficulty-badge ${puzzle.difficulty?.toLowerCase()}`}>
                      {puzzle.difficulty || 'Unknown'}
                    </span>
                  </td>
                  <td>
                    <span className={`status-badge ${puzzle.solvedAt ? 'solved' : 'unsolved'}`}>
                      {puzzle.solvedAt ? 'Solved' : 'Unsolved'}
                    </span>
                  </td>
                  <td>{puzzle.solveTimeMs ? formatTime(puzzle.solveTimeMs) : '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>


    </div>
  );
};

export default Statistics;