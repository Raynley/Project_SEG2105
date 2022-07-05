package com.example.deliverable11;

import java.util.ArrayList;

public class Course {
    private String name;
    private String code;
    private String instructor;
    private ArrayList<Student> student_list;
    private int course_capacity;
    private int number_of_students;
    private String description;
    private String days;
    private String hours;

    public Course(String name, String code) {
        this.name = name;
        this.code = code;
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

    public boolean setInstructor(String instructor) {
        if (this.instructor == null) {
            this.instructor = instructor;
            return true;
        } else {
            return false;
        }
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
    }

    public ArrayList<Student> getStudent_list() {
        return student_list;
    }

    public void addStudent(Student student) {
        student_list.add(student);
        number_of_students++;
    }

    public String toString() {
        String str = name + ":" + code;
        if (instructor != null) {
            str = str + " - Instructor: " + instructor;
        }
        if (course_capacity != 0) {
            str = str + " - course capacity: " + course_capacity;
        }
        if (description != null) {
            str = str + " - description: " + description;
        }
        return str;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Course newcourse) {
        return newcourse.code.equals(code) && newcourse.name.equals(name);
    }
}
