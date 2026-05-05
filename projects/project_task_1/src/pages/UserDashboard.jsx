import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';
import EventDetails from '../components/EventDetails';

function UserDashboard() {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const res = await api.get('/events');
        setEvents(res.data);
      } catch (err) {
        console.error("Failed to fetch events", err);
      }
    };
    fetchEvents();
  }, []);

  return (
    <div>
      <h2 style={{ marginBottom: '2rem' }}>Available Events</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '2rem' }}>
        {events.map((ev) => (
          <div key={ev.id} style={{ display: 'flex', flexDirection: 'column' }}>
            <EventDetails eventData={{...ev, date: ev.event_date, price: Number(ev.price), availableTickets: ev.available_tickets}} />
            <Link to={`/book/${ev.id}`} className="btn-primary" style={{ textAlign: 'center', textDecoration: 'none', marginTop: '1rem' }}>
              Book Tickets
            </Link>
          </div>
        ))}
        {events.length === 0 && <p>No events found.</p>}
      </div>
    </div>
  );
}

export default UserDashboard;
