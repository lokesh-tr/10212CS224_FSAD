function EventDetails({ eventData }) {
  return (
    <div className="event-details glass-panel">
      <div className="event-header">
        <span className="dept-badge">{eventData.department}</span>
        <h2>{eventData.name}</h2>
      </div>
      
      <div className="event-info-grid">
        <div className="info-item">
          <span className="icon">📅</span>
          <div>
            <strong>Date & Time</strong>
            <p>{eventData.date}</p>
          </div>
        </div>
        
        <div className="info-item">
          <span className="icon">📍</span>
          <div>
            <strong>Venue</strong>
            <p>{eventData.venue}</p>
          </div>
        </div>
        
        <div className="info-item">
          <span className="icon">🎟️</span>
          <div>
            <strong>Price</strong>
            <p>${eventData.price.toFixed(2)} per ticket</p>
          </div>
        </div>
        
        <div className="info-item">
          <span className="icon">🟢</span>
          <div>
            <strong>Availability</strong>
            <p className={eventData.availableTickets < 20 ? 'text-urgent' : 'text-success'}>
              {eventData.availableTickets} tickets left
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EventDetails;
