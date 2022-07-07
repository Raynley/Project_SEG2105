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
    private ArrayList<String> student_list;
    private int course_capacity;
    private int number_of_students;
    private String description;
    private String days;
    private String hours;

    public Course(String name, String code) {
        this.name = name;
        this.code = code;
        instructor = "";
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
        if (this.instructor.equals("")) {
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

    public ArrayList<String> getStudent_list() {
        return student_list;
    }

    public boolean addStudent(String student) {
        if (student_list == null) {
            student_list = new ArrayList<>();
            student_list.add(student);
            return true;
        } else {
            if (number_of_students >= course_capacity) {
                return false;
            } else {
                student_list.add(student);
                number_of_students++;
                return true;
            }
        }
    }

    /*
    Will maybe need to change because of implementation
    of the list of students to the database.
     */
    public String toString() {
        String students = "";
        if (student_list != null) {
            if (student_list.size() > 0) {
                for (int i = 0; i < student_list.size(); i++) {
                    students = students + student_list.get(i);
                }
            }
        }
        return name + ":" + code + " - Instructor: " + instructor + " - course capacity: " + course_capacity +
                " - days:" + days +  " - hours:" + hours + "- description: " + description + " - students: " + students;
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
}
