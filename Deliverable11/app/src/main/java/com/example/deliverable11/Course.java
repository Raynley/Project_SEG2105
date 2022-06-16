package com.example.deliverable11;

import java.util.ArrayList;

public class Course {
    private String name;
    private String code;
    private Instructor instructor;
    private ArrayList<Student> student_list;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public ArrayList<Student> getStudent_list() {
        return student_list;
    }

    public void addStudent(Student student) {
        student_list.add(student);
    }
}
