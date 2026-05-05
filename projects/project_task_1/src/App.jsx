import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import { useContext } from 'react';
import { AuthContext, AuthProvider } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import AdminDashboard from './pages/AdminDashboard';
import UserDashboard from './pages/UserDashboard';
import BookingPage from './pages/BookingPage';
import MyBookings from './pages/MyBookings';
import './index.css';

function ProtectedRoute({ children, adminOnly = false }) {
  const { user, loading } = useContext(AuthContext);

  if (loading) return <div>Loading...</div>;
  if (!user) return <Navigate to="/login" />;
  if (adminOnly && user.role !== 'admin') return <Navigate to="/" />;

  return children;
}

function Navigation() {
  const { user, logout } = useContext(AuthContext);

  return (
    <header className="header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <Link to="/" style={{ textDecoration: 'none' }}>
        <h1 style={{ fontSize: '2rem', margin: 0 }}>Event Booking</h1>
      </Link>
      <nav>
        {user ? (
          <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
            <span>Welcome, <strong>{user.name}</strong> ({user.role})</span>
            {user.role === 'admin' ? (
              <>
                <Link to="/admin" style={{ color: '#818cf8', textDecoration: 'none' }}>Dashboard</Link>
                <Link to="/register" style={{ color: '#818cf8', textDecoration: 'none' }}>Create Admin</Link>
              </>
            ) : (
              <Link to="/my-bookings" style={{ color: '#818cf8', textDecoration: 'none' }}>My Bookings</Link>
            )}
            <button onClick={logout} style={{ background: 'transparent', border: '1px solid var(--error)', color: 'var(--error)', padding: '0.4rem 1rem', borderRadius: '8px', cursor: 'pointer' }}>Logout</button>
          </div>
        ) : (
          <div style={{ display: 'flex', gap: '1rem' }}>
            <Link to="/login" style={{ color: '#818cf8', textDecoration: 'none' }}>Login</Link>
            <Link to="/register" style={{ color: '#818cf8', textDecoration: 'none' }}>Register</Link>
          </div>
        )}
      </nav>
    </header>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app-container">
          <Navigation />
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/admin" element={
              <ProtectedRoute adminOnly={true}>
                <AdminDashboard />
              </ProtectedRoute>
            } />
            <Route path="/book/:id" element={
              <ProtectedRoute>
                <BookingPage />
              </ProtectedRoute>
            } />
            <Route path="/my-bookings" element={
              <ProtectedRoute>
                <MyBookings />
              </ProtectedRoute>
            } />
            <Route path="/" element={
              <ProtectedRoute>
                <UserDashboard />
              </ProtectedRoute>
            } />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
