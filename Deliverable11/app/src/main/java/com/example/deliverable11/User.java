package com.example.deliverable11;

public class User {
    private String username;
    private String password;
    private int id;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean verify(String entry) {
        return password.equals(entry);
    }
}
