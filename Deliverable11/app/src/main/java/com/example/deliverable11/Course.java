package com.example.deliverable11;

public class Course {
    private String name;
    private String code;

    public Course(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void edit(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
