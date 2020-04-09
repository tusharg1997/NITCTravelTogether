package com.example.nitctraveltogether;

public class ModelClassIncomingRequest {

    private String date, email;
    public ModelClassIncomingRequest(String email, String date)
    {
        this.date = date;
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }
}
