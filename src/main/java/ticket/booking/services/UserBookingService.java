package main.java.ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.ticket.booking.entities.Ticket;
import main.java.ticket.booking.entities.Train;
import main.java.ticket.booking.util.UserServiceUtil;
import main.java.ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private User user;

    private List<User> userList;

    private  ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH =
            "/Users/abhinavraj/Desktop/irctc/src/main/java/ticket/booking/localDb/users.json";


    public UserBookingService() {
        this.userList = loadUser();
    }

    public List<User> loadUser()
    {
        File users  = new File(USERS_PATH);
        try
        {
            this.userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
//            for (User u : userList) {
//                System.out.println("Loaded User ID: " + u.getUserId());
//            }
            return userList;
            // if we want to map only user then instead of new TypeReference we use User.class;
        }
        catch (IOException e)
        {
            System.out.println("Something is Going to Be Wrong likely to be in DataBase or Server Down");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
    public UserBookingService(User user)
    {
        this.user = user;
        File users  = new File(USERS_PATH);
        try
        {
            userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
            // if we want to map only user then instead of new TypeReference we use User.class;
        }
        catch (IOException e)
        {
            System.out.println("----Something is Going to Be Wrong likely to be in DataBase or Server Down----");
        }
    }

    public Boolean loginUser()
    {
        Optional<User> foundUser = userList.stream().filter(user1->
        {
            return user1.getName().equalsIgnoreCase(this.user.getName()) &&
                    UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword());
        }).findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUp(User user1)
    {
        userList.add(user1);
        saveUserListToFile();
        return Boolean.TRUE;
    }

    private void saveUserListToFile() {
        try {
            File usersFile = new File(USERS_PATH);
            objectMapper.writeValue(usersFile, userList);
        }
        catch(IOException e)
        {
            System.out.println("Unable To save modified User into local Db");
        }
    }

    public void fetchBooking()
    {
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId)
    {
        System.out.println("Tickets Booked Before cancellation : are--->" +user.getTicketsBooked());
        user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId()==ticketId);
        saveUserListToFile();
        System.out.println("Tickets remain after Cancellation of particular Ticket "+user.getTicketsBooked());
        return Boolean.TRUE;
    }

    public List<Train> getTrains(String source, String destination)
    {
        TrainService trainService = new TrainService();
        return trainService.searchTrains(source,destination);
    }

    private void updateUserInUserList(User user) {
        for (int i = 0; i < this.userList.size(); i++) {
            // Log the userId for debugging
            System.out.println("Comparing: " + this.userList.get(i).getUserId() + " with " + user.getUserId());

            // Compare user IDs directly
            if (this.userList.get(i).getUserId().equals(user.getUserId())) {
                System.out.println("Ticket saved for User ID: " + user.getUserId());
                this.userList.set(i, user);
                return;
            }
        }
        // If no user is found
        System.out.println("Ticket not saved - User ID: " + user.getUserId() + " not found in the list.");
    }


    public void bookSeatByUser() {
        Scanner sc = new Scanner(System.in);
        TrainService trainService = new TrainService();

        System.out.println("Enter the source for which you want to book the train");
        String source = sc.nextLine();
        System.out.println("Enter the destination for which you want to book the train");
        String destination = sc.nextLine();

        List<Train> trainList = trainService.searchTrains(source, destination);
        if (trainList.isEmpty()) {
            System.out.println("\uD83D\uDE86 Oops! No trains found for your route. Try a different search! \uD83D\uDD0D\uD83D\uDE0A");
            return;
        }

        System.out.println("List of Trains available to go from " + source + " to " + destination + " : ");
        for (Train train : trainList) {
            System.out.println(train + " with train id :" + train.getTrainId());
        }

        System.out.println("Enter the train id for which you want to book the seat");
        String trainId = sc.nextLine();
        System.out.println("Enter the date of Travel :");
        String dateTravel = sc.nextLine();

        int seatNo = trainService.bookRandomSeat(trainId);
        if (seatNo != -1) {
            System.out.println("✅ Seat booked successfully!");

            // ✅ Get the existing ticket list
            List<Ticket> ticketList = this.user.getTicketsBooked();
            if (ticketList == null) {
                ticketList = new ArrayList<>();
            }

            // ✅ Add the new ticket to the existing list
            Ticket ticket = new Ticket(
                    UUID.randomUUID().toString(), this.user.getUserId(), source,
                    destination, dateTravel, trainService.getTrainById(trainId), seatNo
            );
            ticketList.add(ticket);

            // ✅ Update the user's ticket list
            this.user.setTicketsBooked(ticketList);
            updateUserInUserList(this.user);
            saveUserListToFile();
        } else {
            System.out.println("❌ Seat can't be booked due to some error. Please try again later.");
        }
    }

    public User findByUserId(String userId)
    {
        User foundUser;
        for(User user :this.userList)
        {
            if(user.getUserId().equals(userId))
            {
                foundUser = user;
                return foundUser;
            }
        }
        return null;
    }
}