package main.java.ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import main.java.ticket.booking.services.UserBookingService;

@JsonIgnoreProperties(ignoreUnknown = true)  // This will ignore unknown properties (like ticketInfo)
public class Ticket {

    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private Train train;
    private int seatNo;

    public Ticket() {}

    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, Train train, int seatNo) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
        this.seatNo = seatNo;
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDateOfTravel() { return dateOfTravel; }
    public void setDateOfTravel(String dateOfTravel) { this.dateOfTravel = dateOfTravel; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public int getSeatNo() { return seatNo; }
    public void setSeatNo(int seatNo) { this.seatNo = seatNo; }

    // ticketInfo is generated dynamically, no need to map it with @JsonProperty
    public String getTicketInfo() {
        return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s and having seat No as %s",
                ticketId, userId, source, destination, dateOfTravel, seatNo);
    }
}
