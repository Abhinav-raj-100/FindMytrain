package main.java.ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "/Users/abhinavraj/Desktop/irctc/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() {
        this.trainList = loadTrains();
    }

//    public List<Train> loadTrains() {
//        File trains = new File(TRAINS_PATH);
//        try {
//            return objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
//        } catch (IOException e) {
//            System.out.println("Something is wrong, likely with the database or server down.");
//            return new ArrayList<>();
//        }
//    }
      public List<Train> loadTrains() {
    File trains = new File(TRAINS_PATH);
    try {
        List<Train> trainsLoaded = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
        System.out.println("✅ Loaded " + trainsLoaded.size() + " trains.");
        return trainsLoaded;
    } catch (IOException e) {
//        System.out.println("❌ Error loading trains: " + e.getMessage());
        return new ArrayList<>();
    }
}


    public List<Train> searchTrains(String source, String destination) {
        return this.trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    public int bookRandomSeat(String trainId) {
        Train train = trainList.stream()
                .filter(t -> t.getTrainId().equals(trainId))
                .findFirst()
                .orElse(null);

        if (train == null) {
            System.out.println("❌ Train not found!");
            return -1;
        }

        List<List<Integer>> seats = train.getSeats();
        List<int[]> availableSeats = new ArrayList<>();
        Random random = new Random();

        int totalCols = seats.get(0).size();

        for (int row = 0; row < seats.size(); row++) {
            for (int col = 0; col < seats.get(row).size(); col++) {
                if (seats.get(row).get(col) == 0) {
                    availableSeats.add(new int[]{row, col}); // Store row and column
                }
            }
        }

        if (availableSeats.isEmpty()) {
            System.out.println("❌ No available seats!");
            return -1;
        }

        int randomIndex = random.nextInt(availableSeats.size());
        int[] randomSeat = availableSeats.get(randomIndex);

        int seatNumber = (randomSeat[0] * totalCols) + (randomSeat[1] + 1);
        // Book the seat
        seats.get(randomSeat[0]).set(randomSeat[1], 1);
        train.setSeats(seats);
        train.setAvailable(availableSeats.size() - 1);

        saveTrains();

        System.out.println("🎟️ Seat booked: Seat No. " + seatNumber);

        return seatNumber;
    }

    private void saveTrains() {
        try {
            objectMapper.writeValue(new File(TRAINS_PATH), trainList);
        } catch (IOException e) {
            System.out.println("❌ Error saving train data: " + e.getMessage());
        }
    }

    public Train getTrainById(String trainId) {
        return trainList.stream()
                .filter(train -> train.getTrainId().equals(trainId))
                .findFirst()
                .orElse(null);  // Returns null if no train is found
    }

}
