package main.java.ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Train {
    @JsonProperty("train_Id")
    private String trainId;

    @JsonProperty("train_No")
    private String trainNo;

    @JsonProperty("station_times")
    private Map<String, String> stationTimes;

    @JsonProperty("stations")
    private List<String> stations;

    @JsonProperty("seats")
    private List<List<Integer>> seats;

    @JsonProperty("trainInfo")
    private String trainInfo;

    public void setTrainInfo(String trainInfo) {
        this.trainInfo = trainInfo;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    @JsonProperty("available")
    private int available;

    public Train() {
    }

    public Train (String trainId, String trainNo, List<List<Integer>> seats, Map<String,
                    String> stationTimes, List<String> stations,int available) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
        this.available = available;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getTrainInfo()
    {
        return this.trainInfo;
    }


}
