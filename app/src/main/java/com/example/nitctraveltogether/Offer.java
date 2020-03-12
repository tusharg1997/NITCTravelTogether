package com.example.nitctraveltogether;

public class Offer {

    public String email, destination, availableSeats, vehicleType, time;

    public Offer(String email, String destination, String availableSeats, String vehicleType, String time) {
        this.email = email;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.vehicleType = vehicleType;
        this.time = time;
    }
    public Offer(){

    }
}
