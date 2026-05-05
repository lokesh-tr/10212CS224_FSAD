import { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import api from '../api';

function AdminDashboard() {
  const { user } = useContext(AuthContext);
  const [events, setEvents] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [formData, setFormData] = useState({
    name: '', department: '', event_date: '', venue: '', price: 0, total_tickets: 0
  });

  const fetchEvents = async () => {
    try {
      const res = await api.get('/events');
      setEvents(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings');
      setBookings(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    fetchEvents();
    fetchBookings();
  }, []);

  const handleCreateEvent = async (e) => {
    e.preventDefault();
    try {
      await api.post('/events', formData);
      setFormData({ name: '', department: '', event_date: '', venue: '', price: 0, total_tickets: 0 });
      fetchEvents();
    } catch (err) {
      console.error("Failed to create event", err);
    }
  };

  const handleDeleteEvent = async (id) => {
    if(window.confirm("Are you sure?")) {
      try {
        await api.delete(`/events/${id}`);
        fetchEvents();
      } catch (err) {
        console.error("Failed to delete", err);
      }
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  if (user?.role !== 'admin') return <div className="glass-panel"><h3>Access Denied.</h3></div>;

  return (
    <div style={{ display: 'grid', gap: '2rem' }}>
      <div className="glass-panel">
        <h2>Create New Event</h2>
        <form onSubmit={handleCreateEvent} className="booking-form" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
          <div className="form-group"><label>Event Name</label><input type="text" name="name" value={formData.name} onChange={handleChange} required /></div>
          <div className="form-group"><label>Department</label><input type="text" name="department" value={formData.department} onChange={handleChange} required /></div>
          <div className="form-group"><label>Date & Time</label><input type="text" name="event_date" value={formData.event_date} onChange={handleChange} placeholder="e.g. 15 May 2026, 10:00 AM" required /></div>
          <div className="form-group"><label>Venue</label><input type="text" name="venue" value={formData.venue} onChange={handleChange} required /></div>
          <div className="form-group"><label>Price ($)</label><input type="number" name="price" value={formData.price} onChange={handleChange} required /></div>
          <div className="form-group"><label>Total Tickets</label><input type="number" name="total_tickets" value={formData.total_tickets} onChange={handleChange} required /></div>
          <button type="submit" className="btn-primary" style={{ gridColumn: 'span 2' }}>Create Event</button>
        </form>
      </div>

      <div className="glass-panel">
        <h2>Manage Events</h2>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', textAlign: 'left', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)' }}>
                <th>Name</th><th>Date</th><th>Tickets (Avail/Total)</th><th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {events.map((ev) => (
                <tr key={ev.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                  <td style={{ padding: '0.5rem 0' }}>{ev.name}</td>
                  <td>{ev.event_date}</td>
                  <td>{ev.available_tickets} / {ev.total_tickets}</td>
                  <td><button onClick={() => handleDeleteEvent(ev.id)} style={{ background: '#ef4444', color: 'white', border: 'none', padding: '0.3rem 0.6rem', borderRadius: '4px', cursor: 'pointer' }}>Delete</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="glass-panel">
        <h2>All Bookings</h2>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', textAlign: 'left', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)' }}>
                <th>User</th><th>Email</th><th>Event</th><th>Tickets</th><th>Amount</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                  <td style={{ padding: '0.5rem 0' }}>{b.user_name}</td>
                  <td>{b.user_email}</td>
                  <td>{b.event_name}</td>
                  <td>{b.tickets_count}</td>
                  <td>${b.total_amount}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;
