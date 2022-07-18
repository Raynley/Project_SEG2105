package com.example.deliverable11;

import java.util.ArrayList;

/**Creates course and allows interaction with them
 * @author tannergiddings
 */
public class Course {
    private String name;
    private String code;
    private String instructor;
    private int course_capacity;
    private int number_of_students;
    private String description;
    private String days;
    private String hours;
    private boolean hasInstructor;
    private int index;

    /**Constructor
     * @author tannergiddings
     * @param name name for new course
     * @param code code for new course
     */
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

    /**Constructor without parameters
     * @author tannergiddings
     */
    private Course(){}

    /**getter for course capacity
     * @author tannergiddings
     * @return capacity of the course
     */
    public int getCourse_capacity() {
        return course_capacity;
    }

    /**setter for course_capacity
     * @author tannergiddings
     * @param course_capacity new course capacity
     */
    public void setCourse_capacity(int course_capacity) {
        this.course_capacity = course_capacity;
    }

    /**'getter' for 'name'
     * @author tannergiddings
     * @return name of course
     */
    public String getName() {
        return name;
    }

    /** getter for 'code
     * @author tannergiddings
     * @return
     */
    public String getCode() {
        return code;
    }

    /**'setter' for 'name'
     * @author tannergiddings
     * @param name new name for course
     */
    public void setName(String name) {
        this.name = name;
    }

    /**'setter' for code
     * @author tannergiddings
     * @param code new code for course
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**'setter for instructor and changes hasInstructor to 'true'
     * @author tannergiddings
     * @param instructor new instructor for course. Username will be entered
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
        hasInstructor = true;
    }

    /**'getter' for days
     * @author tannergiddings
     * @return days of course
     */
    public String getDays() {
        return days;
    }

    /**'setter' for days
     * @author tannergiddings
     * @param days new days for course
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**'getter for hours
     * @author tannergiddings
     * @return hours for course
     */
    public String getHours() {
        return hours;
    }

    /**'setter' for hours
     * @author tannergiddings
     * @param hours new hours for course
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**getter for instructor
     * @author tannergiddings
     * @return username of instructor
     */
    public String getInstructor() {
        return instructor;
    }

    /**removes instructor from course
     * @author tannergiddings
     */
    public void removeInstructor() {
        instructor = "";
        hasInstructor = false;
    }

    /**Adds student to course
     * @author tannergiddings
     * @return true if spot in course available. False if not.
     */
    public boolean addStudent() {
        if (course_capacity > 0) {
            if (course_capacity > number_of_students) {
                number_of_students++;
                return true;
            }
        }
        return false;
    }

    /**getter for hasInstructor
     * @author tannergiddings
     * @return if the course has an instructor
     */
    public boolean getHasInstructor(){
        return hasInstructor;
    }

    /**Returns a string value of the course
     * @author tanner giddings
     * @return String of the elements of the course
     */
    public String toString() {
        return name + ":" + code + " - Instructor: " + instructor + " - course capacity: " + course_capacity +
                " - days:" + days +  " - hours:" + hours + " - description: " + description + " - number of students" + number_of_students;
    }

    /**setter for description
     * @author tannergiddings
     * @param description course description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**Verifies if a course equals another. Compares the names and codes
     * @author tannergiddings
     * @param newcourse Course to be compared
     * @return if the courses equals each other
     */
    public boolean equals(Course newcourse) {
        return newcourse.code.equals(code) && newcourse.name.equals(name);
    }

    /**toString() method for viewing for students
     * @author tannergiddings
     * @return String
     */
    public String stud_toString() {
        return name + ":" + code + " - Instructor: " + instructor + " - course capacity: " + course_capacity +
                " - days:" + days +  " - hours:" + hours + "- description: " + description +
                " - number of students: " + number_of_students;
    }

    /**Basic toString() method
     * @author tannergiddings
     * @return name : code
     */
    public String basicToString() {
        return name + " : " + code;
    }

    /**getter for index
     * @author tannergiddings
     * @return index of course
     */
    public int getIndex() {
        return index;
    }

    /**setter for index
     * @author tannergiddings
     * @param index new index for course
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
