package com.example.deliverable11;

public class User {
    private String username;
    private String password;
    private int index;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean verify(String entry) {
        return password.equals(entry);
    }

    public String toString() {
        return username;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

