package com.example.deliverable11;

import java.util.ArrayList;

public class Course {
    private String name;
    private String code;
    private String instructor;
    /*
    Possibly need to change student list
    to a database kind of thing
     */
    private int course_capacity;
    private int number_of_students;
    private String description;
    private String days;
    private String hours;
    private boolean hasInstructor;
    private int index;

    public Course(String name, String code) {
        this.name = name;
        this.code = code;
        instructor = "";
        hasInstructor = false;
        description = "";
        days = "";
        hours = "";
        course_capacity = 0;
        number_of_students = 0;
    }

    private Course(){}

    public int getCourse_capacity() {
        return course_capacity;
    }

    public void setCourse_capacity(int course_capacity) {
        this.course_capacity = course_capacity;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
        hasInstructor = true;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getInstructor() {
        return instructor;
    }

    public void removeInstructor() {
        instructor = "";
        hasInstructor = false;
    }

    public boolean addStudent() {
        if (course_capacity > number_of_students) {
            number_of_students++;
            return true;
        }
        return false;
    }

    public boolean getHasInstructor(){
        return hasInstructor;
    }

    /*
    Will maybe need to change because of implementation
    of the list of students to the database.
     */
    public String toString() {
        return name + ":" + code + " - Instructor: " + instructor + " - course capacity: " + course_capacity +
                " - days:" + days +  " - hours:" + hours + " - description: " + description + " - number of students" + number_of_students;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Course newcourse) {
        return newcourse.code.equals(code) && newcourse.name.equals(name);
    }

    public String stud_toString() {
        return name + ":" + code + " - Instructor: " + instructor + " - course capacity: " + course_capacity +
                " - days:" + days +  " - hours:" + hours + "- description: " + description +
                " - number of students: " + number_of_students;
    }

    public String basicToString() {
        return name + " : " + code;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
