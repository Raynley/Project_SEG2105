package com.example.deliverable11;

public class User {
    private String username;
    private String password;
    private int id;

    public User(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }



    public boolean verify(String entry) {
        return password.equals(entry);
    }
}
