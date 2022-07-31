package com.example.deliverable11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private String times;
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
                " - times: " + times + " - description: " + description + " - number of students" + number_of_students;
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
                " - times: " + times + "- description: " + description +
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

    public int getNumber_of_students() {
        return number_of_students;
    }

    public void remove_student() {
        number_of_students--;
    }

    /*
    private String toStringMap(Map<String, ArrayList<Integer>> timeMap) {
        boolean isFirst = true;
        String time_string = "";
        for (String day : times.keySet()) {
            if (!isFirst) {
                time_string += ",";
            } else {
                isFirst = false;
            }
            time_string += day + " " + convertListToString(times.get(day));
        }
        return time_string;
    }
    */


    /**
     * toString() method for time
     */
    /*
    public String toStringMap() {
        return toStringMap(times);
    }

     */

    /**
     * setter for times
     * @param times new times
     */
    public void setTimes(String times) {
        this.times = times;
    }

    /**
     * getter for times
     * @return times
     */
    public String getTimes() {
        return times;
    }

    /**
     * getter for times
     * @return times for this course
     */
    /*
    public Map<String, ArrayList<Integer>> getTimes() {
        return times;
    }

     */

    /**
     * Converts the days in the map of times into a List<String>
     */
    /*
    public ArrayList<String> getDays() {
        ArrayList<String> days = new ArrayList<>();
        for (String day : times.keySet()) {
            days.add(day);
        }
        return days;
    }
    */

    private String convertListToString(ArrayList<Integer> time) {
        return convertIntToTime(time.get(0)) + "-" + convertIntToTime(time.get(1));
    }

    /**
     * Converts number to time
     * @param time_number number for time
     * @return String value of time
     */
    private String convertIntToTime(int time_number) {
        int hour = 0;
        int minute;
        while (time_number > 60) {
            time_number = time_number - 60;
            hour++;
        }
        minute = time_number;
        return hour + ":" + minute;
    }

    public ArrayList<String> getDays() {
        String time = times;
        String[] times_split = time.split(",");
        String[] day_and_time;
        ArrayList<String> days = new ArrayList<>();
        for (int i = 0; i < times_split.length; i++) {
            day_and_time = times_split[i].split(" ");
            for (int j = 0; i < day_and_time.length; j++) {
                if (isDay(day_and_time[j])) {
                    days.add(day_and_time[j]);
                }
            }
        }
        return days;
    }

    /**
     * Finds if a string is a day
     */
    private boolean isDay(String entry) {
        entry = entry.trim();
        if (entry.equals("Monday") || entry.equals("monday") || entry.equals("Mondays") || entry.equals("mondays")
                || entry.equals("Tuesday") || entry.equals("tuesday") || entry.equals("Tuesdays") || entry.equals("tuesdays")
                || entry.equals("Wednesday") || entry.equals("wednesday") || entry.equals("Wednesdays") || entry.equals("wednesdays")
                || entry.equals("Thursday") || entry.equals("thursday") || entry.equals("Thursdays") || entry.equals("thursdays")
                || entry.equals("Friday") || entry.equals("friday") || entry.equals("Fridays") || entry.equals("fridays")
                || entry.equals("Saturday") || entry.equals("saturday") || entry.equals("Saturdays") || entry.equals("saturdays")
                || entry.equals("Sunday") || entry.equals("sunday") || entry.equals("Sundays") || entry.equals("sundays")) {
            return true;
        }
        return false;
    }

    /**
     * Returns the day and times in a map format
     */
    public Map<String, ArrayList<Integer>> returnMap() {
        Map<String, ArrayList<Integer>> timesMap = new HashMap<>();
        ArrayList<Integer> time_list;
        String[] times_split = times.split(",");
        String day;
        String hour_spread;
        String[] hours_spread2;
        String[] this_split;
        int start_time;
        int end_time;

        for (int i = 0; i < times_split.length; i++) {
            time_list = new ArrayList<>();
            this_split = times_split[i].split(" ");
            day = this_split[0];
            hour_spread = this_split[1];
            hours_spread2 = hour_spread.split("-");
            start_time = convertToInt(hours_spread2[0]);
            end_time = convertToInt(hours_spread2[1]);
            if (start_time >= 0 && end_time >= 0) {
                if (start_time <= end_time) {
                    time_list.add(start_time);
                    time_list.add(end_time);
                } else {
                    time_list.add(end_time);
                    time_list.add(start_time);
                }
                timesMap.put(day, time_list);
            }
        }
        return timesMap;
    }

    private int convertToInt(String entry) {
        String[] entry_splits = entry.split(":");
        int[] int_entry_splits = new int[2];
        for (int i = 0; i < 2; i++) {
            int_entry_splits[i] = convertStringToInt(entry_splits[i]);
            if (int_entry_splits[i] == -1) {
                return -1;
            }
        }
        return int_entry_splits[0] * 60 + int_entry_splits[1];
    }

    /**
     * Converts String to int
     * @param entry String entry to convert to int
     * @return int value of entry
     */
    public static int convertStringToInt(String entry) {
        try {
            return Integer.parseInt(entry.trim());
        } catch(NumberFormatException e) {
            return -1;
        }
    }

}
