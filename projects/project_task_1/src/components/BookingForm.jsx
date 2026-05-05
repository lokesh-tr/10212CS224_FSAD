import { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

function BookingForm({ eventData, onBook }) {
  const { user } = useContext(AuthContext);
  const [formData, setFormData] = useState({
    name: user?.name || '',
    email: user?.email || '',
    department: user?.department || '',
    tickets: 1
  });

  const [errors, setErrors] = useState({});

  const validate = () => {
    const newErrors = {};
    if (!formData.name.trim()) newErrors.name = 'Name is required';
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Email is invalid';
    }
    if (!formData.department.trim()) newErrors.department = 'Department is required';
    
    const ticketCount = parseInt(formData.tickets, 10);
    if (!formData.tickets) {
      newErrors.tickets = 'Number of tickets is required';
    } else if (isNaN(ticketCount) || ticketCount <= 0) {
      newErrors.tickets = 'Please enter a positive number';
    } else if (ticketCount > eventData.availableTickets) {
      newErrors.tickets = `Only ${eventData.availableTickets} tickets available`;
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      onBook({
        name: formData.name,
        email: formData.email,
        department: formData.department,
        tickets: parseInt(formData.tickets, 10)
      });
      // form state reset
      setFormData({ name: user?.name || '', email: user?.email || '', department: user?.department || '', tickets: 1 });
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    if (errors[e.target.name]) {
      setErrors({ ...errors, [e.target.name]: '' });
    }
  };

  return (
    <div className="booking-form-container glass-panel">
      <h2>Book Your Tickets</h2>
      {eventData.availableTickets === 0 ? (
        <div className="sold-out">
          <h3>SOLD OUT</h3>
          <p>Sorry, there are no tickets available for this event.</p>
        </div>
      ) : (
        <form onSubmit={handleSubmit} className="booking-form" noValidate>
          <div className="form-group">
            <label htmlFor="name">Full Name</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className={errors.name ? 'error-input' : ''}
              placeholder="John Doe"
            />
            {errors.name && <span className="error-message">{errors.name}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="email">Email ID</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={errors.email ? 'error-input' : ''}
              placeholder="john@example.com"
            />
            {errors.email && <span className="error-message">{errors.email}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="department">Department</label>
            <input
              type="text"
              id="department"
              name="department"
              value={formData.department}
              onChange={handleChange}
              className={errors.department ? 'error-input' : ''}
              placeholder="e.g. Computer Science"
            />
            {errors.department && <span className="error-message">{errors.department}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="tickets">Number of Tickets</label>
            <input
              type="number"
              id="tickets"
              name="tickets"
              min="1"
              max={eventData.availableTickets}
              value={formData.tickets}
              onChange={handleChange}
              className={errors.tickets ? 'error-input' : ''}
            />
            {errors.tickets && <span className="error-message">{errors.tickets}</span>}
          </div>

          <button type="submit" className="btn-primary submit-btn">
            Confirm Booking
          </button>
        </form>
      )}
    </div>
  );
}

export default BookingForm;
