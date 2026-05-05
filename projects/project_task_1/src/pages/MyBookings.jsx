import { useState, useEffect } from 'react';
import api from '../api';

function MyBookings() {
  const [bookings, setBookings] = useState([]);

  const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings');
      setBookings(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handleCancel = async (id) => {
    if (window.confirm("Are you sure you want to cancel this booking?")) {
      try {
        await api.delete(`/bookings/${id}`);
        fetchBookings(); // Refresh list after cancellation
      } catch (err) {
        alert(err.response?.data?.error || "Failed to cancel booking");
      }
    }
  };

  return (
    <div className="glass-panel">
      <h2>My Bookings</h2>
      {bookings.length === 0 ? (
        <p>You have no bookings yet.</p>
      ) : (
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', textAlign: 'left', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--border-color)' }}>
                <th>Event</th>
                <th>Tickets Booked</th>
                <th>Total Paid</th>
                <th>Booking Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                  <td style={{ padding: '1rem 0' }}>{b.event_name}</td>
                  <td>{b.tickets_count}</td>
                  <td>${b.total_amount}</td>
                  <td>{new Date(b.booking_date).toLocaleDateString()}</td>
                  <td>
                    <button
                      onClick={() => handleCancel(b.id)}
                      style={{ background: '#ef4444', color: 'white', border: 'none', padding: '0.4rem 0.8rem', borderRadius: '4px', cursor: 'pointer' }}
                    >
                      Cancel
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default MyBookings;
