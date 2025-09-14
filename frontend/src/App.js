import React, { useState } from 'react';
import { Puzzle, BarChart3, Info } from 'lucide-react';
import SudokuSolver from './components/SudokuSolver';
import Statistics from './components/Statistics';
import './styles/index.css';

const App = () => {
  const [activeTab, setActiveTab] = useState('solver');

  const Header = () => (
    <header className="header">
      <div className="logo">
        <Puzzle size={24} style={{ marginRight: '0.5rem' }} />
        Sudoku Solver Pro
      </div>
      <nav className="nav">
        <button
          className={`nav-button ${activeTab === 'solver' ? 'active' : ''}`}
          onClick={() => setActiveTab('solver')}
        >
          <Puzzle size={16} />
          Solver
        </button>
        <button
          className={`nav-button ${activeTab === 'statistics' ? 'active' : ''}`}
          onClick={() => setActiveTab('statistics')}
        >
          <BarChart3 size={16} />
          Statistics
        </button>
        <button
          className={`nav-button ${activeTab === 'about' ? 'active' : ''}`}
          onClick={() => setActiveTab('about')}
        >
          <Info size={16} />
          About
        </button>
      </nav>
    </header>
  );

  const About = () => (
    <div className="sudoku-container">
      <h2>About Sudoku Solver Pro</h2>
      <div style={{ lineHeight: '1.6', color: '#666' }}>
        <p>
          <strong>Sudoku Solver Pro</strong> is a comprehensive web application that can solve any Sudoku puzzle 
          using advanced algorithms. Built with modern technologies for optimal performance and user experience.
        </p>
        
        <div style={{ 
          background: '#f8f9fa', 
          padding: '1.5rem', 
          borderRadius: '8px', 
          margin: '2rem 0',
          textAlign: 'center',
          border: '2px solid #667eea'
        }}>
          <h3 style={{ color: '#667eea', marginBottom: '1rem' }}>🖥️ Desktop Application Available!</h3>
          <p style={{ marginBottom: '1rem' }}>Download our Java desktop application for offline use with enhanced features.</p>
          <a 
            href="/SudokuSolverDesktop.jar" 
            download="SudokuSolverDesktop.jar"
            style={{
              display: 'inline-block',
              background: '#667eea',
              color: 'white',
              padding: '12px 24px',
              borderRadius: '6px',
              textDecoration: 'none',
              fontWeight: 'bold',
              margin: '0 10px'
            }}
          >
            📥 Download Desktop App
          </a>
          <div style={{ marginTop: '1rem', fontSize: '0.9rem', color: '#666' }}>
            <p><strong>Requirements:</strong> Java 8 or higher</p>
            <p><strong>How to run:</strong> Double-click the JAR file or run: <code>java -jar SudokuSolverDesktop.jar</code></p>
          </div>
        </div>
        
        <h3 style={{ marginTop: '2rem', color: '#333' }}>Features</h3>
        <ul style={{ marginLeft: '1.5rem' }}>
          <li>🧩 <strong>Multiple Solving Algorithms:</strong> Backtracking and Constraint Propagation</li>
          <li>🎯 <strong>Puzzle Generation:</strong> Create puzzles with 4 difficulty levels</li>
          <li>👁️ <strong>Solution Visualization:</strong> Watch the algorithm solve step-by-step</li>
          <li>✅ <strong>Puzzle Validation:</strong> Verify if your puzzle is valid</li>
          <li>📊 <strong>Statistics Dashboard:</strong> Track your solving performance</li>
          <li>💾 <strong>Import/Export:</strong> Save and load puzzles</li>
          <li>📱 <strong>Responsive Design:</strong> Works on all devices</li>
        </ul>

        <h3 style={{ marginTop: '2rem', color: '#333' }}>How to Use</h3>
        <ol style={{ marginLeft: '1.5rem' }}>
          <li>Generate a new puzzle or input your own</li>
          <li>Choose your preferred solving algorithm</li>
          <li>Enable visualization to see the solving process</li>
          <li>Click "Solve" to get the solution instantly</li>
          <li>View statistics to track your progress</li>
        </ol>

        <h3 style={{ marginTop: '2rem', color: '#333' }}>Technology Stack</h3>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginTop: '1rem' }}>
          <div>
            <h4>Backend</h4>
            <ul style={{ listStyle: 'none', padding: 0 }}>
              <li>☕ Java 17</li>
              <li>🍃 Spring Boot 3.2</li>
              <li>🗄️ H2 Database</li>
              <li>🔧 Maven</li>
            </ul>
          </div>
          <div>
            <h4>Frontend</h4>
            <ul style={{ listStyle: 'none', padding: 0 }}>
              <li>⚛️ React 18</li>
              <li>📊 Recharts</li>
              <li>🎨 CSS3</li>
              <li>🔥 React Hot Toast</li>
            </ul>
          </div>
        </div>

        <div style={{ 
          background: '#f8f9fa', 
          padding: '1.5rem', 
          borderRadius: '8px', 
          marginTop: '2rem',
          textAlign: 'center'
        }}>
          <p style={{ margin: 0, fontWeight: '500' }}>
            Built with ❤️ for Sudoku enthusiasts and algorithm lovers
          </p>
          <p style={{ margin: '0.5rem 0 0 0', fontSize: '0.9rem', color: '#666' }}>
            Available as Web App and Desktop Application
          </p>
        </div>
      </div>
    </div>
  );

  const renderContent = () => {
    switch (activeTab) {
      case 'solver':
        return <SudokuSolver />;
      case 'statistics':
        return <Statistics />;
      case 'about':
        return <About />;
      default:
        return <SudokuSolver />;
    }
  };

  return (
    <div className="app">
      <Header />
      <main className="main-content">
        {renderContent()}
      </main>
    </div>
  );
};

export default App;