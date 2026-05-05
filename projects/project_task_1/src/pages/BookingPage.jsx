import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api';
import EventDetails from '../components/EventDetails';
import BookingForm from '../components/BookingForm';

function BookingPage() {
  const { id } = useParams();
  const [eventData, setEventData] = useState(null);
  const [bookingSummary, setBookingSummary] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const res = await api.get(`/events/${id}`);
        const data = res.data;
        data.date = data.event_date; // map db field to component prop
        data.availableTickets = data.available_tickets;
        data.price = Number(data.price);
        setEventData(data);
      } catch (err) {
        setError('Failed to load event details.');
      }
    };
    fetchEvent();
  }, [id]);

  const handleBooking = async (bookingDetails) => {
    try {
      const res = await api.post('/bookings', {
        event_id: id,
        tickets_count: bookingDetails.tickets
      });
      
      setEventData(prev => ({
        ...prev,
        availableTickets: prev.availableTickets - bookingDetails.tickets
      }));

      setBookingSummary({
        ...bookingDetails, // From the form data
        eventName: res.data.event_name,
        totalAmount: res.data.total_amount
      });
    } catch (err) {
      alert(err.response?.data?.error || "Booking failed");
    }
  };

  const handleReset = () => {
    setBookingSummary(null);
    // Re-fetch event to get current tickets just in case
    api.get(`/events/${id}`).then(res => {
      const data = res.data;
      data.date = data.event_date;
      data.availableTickets = data.available_tickets;
      data.price = Number(data.price);
      setEventData(data);
    });
  };

  if (error) return <div className="glass-panel text-urgent">{error}</div>;
  if (!eventData) return <div className="glass-panel">Loading event...</div>;

  return (
    <div className="main-content">
      <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        <Link to="/" style={{ color: '#818cf8', textDecoration: 'none' }}>&larr; Back to Dashboard</Link>
        <EventDetails eventData={eventData} />
      </div>
      
      {!bookingSummary ? (
        <BookingForm eventData={eventData} onBook={handleBooking} />
      ) : (
        <div className="booking-summary glass-panel">
          <h2>Booking Confirmation</h2>
          <div className="success-icon">✓</div>
          <p>Your tickets have been successfully booked!</p>
          <div className="summary-details">
            <p><strong>Name:</strong> {bookingSummary.name}</p>
            <p><strong>Email:</strong> {bookingSummary.email}</p>
            <p><strong>Event:</strong> {bookingSummary.eventName}</p>
            <p><strong>Tickets Booked:</strong> {bookingSummary.tickets}</p>
            <p><strong>Total Amount:</strong> ${parseFloat(bookingSummary.totalAmount).toFixed(2)}</p>
          </div>
          <button className="btn-primary reset-btn" onClick={handleReset}>Book Another Ticket</button>
        </div>
      )}
    </div>
  );
}

export default BookingPage;
