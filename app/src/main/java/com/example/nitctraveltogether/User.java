package com.example.nitctraveltogether;

public class User {

    public String email_id, firstName, lastName, age;
    private String password,phone;
    public User(){

    }
    public User(String email, String password, String name, String lname, String phone, String age)
    {
        this.email_id   = email;
        this.password = password;
        this.firstName = name;
        this.lastName = lname;
        this.phone = phone;
        this.age = age;
    }
    public String getPassword(){
        return password;
    }
    public String getPhone(){
        return phone;
    }
}
