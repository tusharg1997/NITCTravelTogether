package com.nitc.nitctraveltogether;

public class ModelClass {

    private String name, email;
   public ModelClass(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
