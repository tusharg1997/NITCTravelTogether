package com.example.nitctraveltogether;

public class Offer {

    public String email, destination, availableSeats, vehicleType, time,id;

    public Offer(String email, String destination, String availableSeats, String vehicleType, String time, String id) {
        this.email = email;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.vehicleType = vehicleType;
        this.time = time;
        this.id = id;
    }
    public Offer(){

    }
}
