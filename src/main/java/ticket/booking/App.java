package main.java.ticket.booking;

import main.java.ticket.booking.entities.Ticket;
import main.java.ticket.booking.entities.Train;
import main.java.ticket.booking.entities.User;
import main.java.ticket.booking.services.UserBookingService;
import main.java.ticket.booking.util.UserServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class App {

    private static Optional<User> foundUser = Optional.empty();

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner sc = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService = new UserBookingService();

        while (option != 7) {
            System.out.println("\nChoose Option: ");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("7. Exit the app");
            option = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.println("Enter Your Username to sign up: ");
                    String nameToSignup = sc.nextLine();

                    System.out.println("Enter your password: ");
                    String passwordToSignup = sc.nextLine();

                    // Create new user and save to database
                    User userToSignup = new User(
                            nameToSignup,
                            passwordToSignup,
                            UserServiceUtil.hashPassword(passwordToSignup),
                            UUID.randomUUID().toString(), // Generates a new unique user ID
                            new ArrayList<>());

                    if (userBookingService.signUp(userToSignup)) {
                        System.out.println("User Successfully Signed Up!");
                    } else {
                        System.out.println("Something went wrong. Try again.");
                    }
                    break;

                case 2:
                    System.out.println("Enter your Username to login: ");
                    String userName = sc.nextLine();

                    System.out.println("Enter your Password: ");
                    String passwordToLogin = sc.nextLine();

                    // Load users and find the existing user
                    List<User> allUsers = userBookingService.loadUser();
                     foundUser = allUsers.stream()
                            .filter(u -> u.getName().equalsIgnoreCase(userName) &&
                                    UserServiceUtil.checkPassword(passwordToLogin, u.getHashedPassword()))
                            .findFirst();

                    if (foundUser.isPresent()) {
                        System.out.println("Successfully logged in!");
                        userBookingService = new UserBookingService(foundUser.get()); // ✅ Use existing user

                        int loginOption = 0;
                        while (loginOption != 7) {
                            System.out.println("\nChoose Option:");
                            System.out.println("3. Fetch Bookings");
                            System.out.println("4. Search Trains");
                            System.out.println("5. Book a Seat");
                            System.out.println("6. Cancel my Booking");
                            System.out.println("7. Exit the app");
                            loginOption = sc.nextInt();
                            sc.nextLine(); // Consume newline

                            switch (loginOption) {
                                case 3:
                                    System.out.println("Fetching your bookings...");
                                    userBookingService.fetchBooking();
                                    break;

                                case 4:
                                    System.out.println("Enter Source Station:");
                                    String source = sc.nextLine();
                                    System.out.println("Enter Destination Station:");
                                    String destination = sc.nextLine();
                                    List<Train> getTrains = userBookingService.getTrains(source, destination);
                                    if (!getTrains.isEmpty()) {
                                        System.out.println("Trains Available:");
                                        for (Train train : getTrains) {
                                            System.out.println(train.getTrainInfo());
                                        }
                                    } else {
                                        System.out.println("No Trains Found between " + source + " and " + destination);
                                    }
                                    break;

                                case 5:
                                    userBookingService.bookSeatByUser();
                                    break;

                                case 6:
                                    List<Ticket> bookedTickets = foundUser.get().getTicketsBooked();
                                    for(Ticket ticket :bookedTickets)
                                    {
                                        System.out.println(ticket.getTicketId());
                                    }
                                    System.out.println("Enter Ticket ID to " +
                                            "Cancel: Id as index from list of ticket above");
                                    int ticketId = sc.nextInt();
                                    Ticket chooseTicket = bookedTickets.get(ticketId-1);
                                    boolean cancelled = userBookingService.cancelBooking(chooseTicket.getTicketId());
                                    if (cancelled) {
                                        System.out.println("Booking Cancelled Successfully!");
                                    } else {
                                        System.out.println("Could not cancel booking. Please check Ticket ID.");
                                    }
                                    break;

                                case 7:
                                    System.out.println("Logging out...");
                                    break;

                                default:
                                    System.out.println("Invalid selection. Try again.");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Wrong credentials! Try again.");
                    }
                    break;

                case 7:
                    System.out.println("Exiting the application...");
                    break;

                default:
                    System.out.println("Invalid selection! Choose a valid option.");
            }
        }
        sc.close();
    }
}
