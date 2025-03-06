package main.java.ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class User {

    @JsonProperty("name")
    private String name;


    @JsonProperty("password")
    private String password;

    @JsonProperty ("hashed_password")
    private String hashedPassword;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("tickets_booked")
    private List<Ticket> ticketsBooked;

    public User() {
    }

    public User(String name, String password, String hashedPassword, String userId, List<Ticket> ticketsBooked) {
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.userId = userId;
        this.ticketsBooked = ticketsBooked;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public void printTickets()
    {
        if(ticketsBooked.size()==0)
        {
            System.out.println("---------Yet Now You have No any bookings Kindly Book first !! -----");
            return;
        }
        for(int i =0;i<ticketsBooked.size();i++)
        {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

}
