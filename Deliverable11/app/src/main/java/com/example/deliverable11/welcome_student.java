package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Methods and functionnalities for welcome_student.xml
 * @author tannergiddings
 */
public class welcome_student extends AppCompatActivity {
    EditText name_entry, code_entry, day_entry;
    ImageButton add_btn, remove_btn, search_btn;
    TextView displayCourses, error_display, course_view;
    ArrayList<Course> courseList;
    FirebaseDatabase database;
    DatabaseReference reference, student_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_student);
        this.setTitle("Welcome student");

        name_entry = findViewById(R.id.course_name_to_edit);
        code_entry = findViewById(R.id.course_code_to_edit);
        day_entry = findViewById(R.id.daySearch);
        add_btn = findViewById(R.id.add_btn);
        remove_btn = findViewById(R.id.delete_course_btn);
        search_btn = findViewById(R.id.search_btn);
        displayCourses = findViewById(R.id.displayCourse);
        error_display = findViewById(R.id.Error);
        course_view = findViewById(R.id.Enrol_View);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        student_reference = database.getReference("Students");
        courseList = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Integer> enrolled_courses_id = new ArrayList<>();
        Map<String, Integer> student_keys_list = new HashMap<>();
        //Day, then hours
        Map<String, ArrayList<Integer>> schedule = new HashMap<>();

        ValueEventListener initList = new ValueEventListener() {
            /**Initialises courseList
             * @author tannergiddings
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    courseList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        courseList.add(ds.getValue(Course.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        /**
         * Use on student_reference
         */
        ValueEventListener init_schedule = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username;
                    ArrayList<Integer> course_indices = new ArrayList<>();
                    if (savedInstanceState == null) {
                        Bundle b = getIntent().getExtras();
                        if (b == null) {
                            username = null;
                        } else {
                            username = b.getString("USERNAME");
                        }
                    } else {
                        username = (String) savedInstanceState.getSerializable("USERNAME");
                    }

                    schedule.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot ds_2 : ds.getChildren()) {
                            if (ds_2.child("username").getValue(String.class).equals(username)) {
                                course_indices.add(Integer.parseInt(ds.getKey()));
                                break;
                            }
                        }
                    }
                    reference.addValueEventListener(initList);
                    ArrayList<Course> courses = findCoursesByIds(course_indices, courseList);
                    String day;
                    int start_time;
                    int end_time;
                    String beginning;
                    String end;
                    String time;
                    String[] times_split;
                    String[] days_and_time;
                    String[] times_only;
                    int swap;
                    ArrayList<Integer> times_list = new ArrayList<>();

                    for (int i = 0; i < courses.size(); i++) {
                        time = courses.get(i).getTimes();
                        times_split = time.split(",");
                        for (int j = 0; j < times_split.length; i++) {
                            days_and_time = times_split[j].split(" ");
                            day = days_and_time[0];
                            times_only = days_and_time[1].split("-");
                            start_time = convertToInt(times_only[0]);
                            end_time = convertToInt(times_only[1]);
                            if (end_time < start_time) {
                                swap = end_time;
                                end_time = start_time;
                                start_time = swap;
                            }

                            if (day != null && start_time >= 0 && end_time >= 0) {
                                times_list.add(start_time);
                                times_list.add(end_time);
                                schedule.put(day, times_list);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        ValueEventListener init_student_keys = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    student_keys_list.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        student_keys_list.put(ds.child("username").getValue(String.class), ds.child("index").getValue(Integer.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener postListener = new ValueEventListener() {
            /**Displays the course to the user
             * @author tannergiddings
             * @param snapshot database snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Course.class).stud_toString() + "\n";
                    }
                    displayCourses.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        ValueEventListener init_students = new ValueEventListener() {
            /**Initialises list of students, a.k.a students
             * @author tannergiddings
             * @param snapshot database snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        students.add(ds.getValue(Student.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener init_enrolled_courses = new ValueEventListener() {
            /**Gets the courses in which the user is enrolled in
             * @author tannergiddings
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.addValueEventListener(initList);
                    String username;
                    ArrayList<Integer> key_list = new ArrayList<>();
                    ArrayList<Course> course_list;

                    if (savedInstanceState == null) {
                        Bundle b = getIntent().getExtras();
                        if (b == null) {
                            username = null;
                        } else {
                            username = b.getString("USERNAME");
                        }
                    } else {
                        username = (String) savedInstanceState.getSerializable("USERNAME");
                    }

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot ds_sub : ds.getChildren()) {
                            if (ds_sub.child("username").getValue().equals(username)) {
                                key_list.add(Integer.parseInt(ds.getKey()));
                                break;
                            }
                        }
                    }

                    course_list = findCoursesByIds(key_list, courseList);
                    String text = "";

                    for (int i = 0; i < course_list.size(); i++) {
                        text = text + course_list.get(i).basicToString() + "\n";
                    }

                    course_view.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        student_reference.addValueEventListener(init_enrolled_courses);
        reference.addValueEventListener(postListener);
        add_btn.setOnClickListener(new View.OnClickListener() {
            /**Adds the student to the course
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                reference.addValueEventListener(initList);
                String username;

                if (savedInstanceState == null) {
                    Bundle b = getIntent().getExtras();
                    if (b == null) {
                        username = null;
                    } else {
                        username = b.getString("USERNAME");
                    }
                } else {
                    username = (String) savedInstanceState.getSerializable("USERNAME");
                }

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(code)) {
                    name_entry.setText("Name required");
                    code_entry.setText("Code required");
                    error_display.setText("");
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    name_entry.setText("Name required");
                    error_display.setText("");
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    code_entry.setText("Code required");
                    error_display.setText("");
                    return;
                }

                int index = getIndex(new Course(name, code), courseList);

                if (index < 0) {
                    error_display.setText("Course not found");
                    return;
                } else {
                    student_reference.child(String.valueOf(index)).addValueEventListener(init_students);
                    boolean inCourse = inCourse(username, students);
                    if (inCourse) {
                        error_display.setText("You are already enrolled in this course");
                        return;
                    } else {
                        student_reference.addValueEventListener(init_schedule);
                        Course current = findCourse(index, courseList);
                        if (verify_schedule(schedule, current)) {
                            student_reference.child(String.valueOf(current.getIndex())).addValueEventListener(init_student_keys);
                            if (!student_keys_list.containsKey(username)) {
                                if (current.addStudent()) {
                                    reference.child(String.valueOf(index)).child("number_of_students").setValue(current.getNumber_of_students());
                                    Student current_student = new Student();
                                    current_student.setUsername(username);
                                    current_student.setIndex(createIndexStudents(students));
                                    student_reference.child(String.valueOf(index)).child(String.valueOf(current_student.getIndex())).setValue(current_student);
                                    error_display.setText("");
                                    name_entry.setText("");
                                    code_entry.setText("");
                                } else {
                                    error_display.setText("Course at capacity. Enrolment failed");
                                }
                                return;
                            } else {
                                error_display.setText("You are already enrolled in this course");
                            }
                        } else {
                            error_display.setText("You have a time conflict");
                        }
                    }
                }
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            /**Allows user to search for a course based on days offered, code or name
             * @author tannergiddings
             * @param v
             */

            @Override
            public void onClick(View v) {
                reference.addValueEventListener(initList);
                String name = name_entry.getText().toString().trim();
                String code = code_entry.getText().toString().trim();
                String days = day_entry.getText().toString().trim();
                ArrayList<Course> courses_to_display = courseList;

                if (!TextUtils.isEmpty(name)) {
                    courses_to_display = compareName(courses_to_display, name);
                }

                if (!TextUtils.isEmpty(code)) {
                    courses_to_display = compareCode(courses_to_display, code);
                }

                if (!TextUtils.isEmpty(days)) {
                    courses_to_display = findDaysAmongCourses(courses_to_display, days);
                }

                displayCourses.setText(toStringList(courses_to_display));
            }
        });

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                reference.addValueEventListener(initList);
                String username;
                if (savedInstanceState == null) {
                    Bundle b = getIntent().getExtras();
                    if (b == null) {
                        username = null;
                    } else {
                        username = b.getString("USERNAME");
                    }
                } else {
                    username = (String) savedInstanceState.getSerializable("USERNAME");
                }
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(code)) {
                    name_entry.setText("Name required");
                    code_entry.setText("Code required");
                    error_display.setText("");
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    name_entry.setText("Name required");
                    error_display.setText("");
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    code_entry.setText("Code required");
                    error_display.setText("");
                    return;
                }
                student_reference.addValueEventListener(init_enrolled_courses);
                ArrayList<Course> enrolled_courses = findCoursesByIds(enrolled_courses_id, courseList);
                Course current = findCourseInList(courseList, new Course(name, code));

                if (current == null) {
                    error_display.setText("Course not found");
                    return;
                } else {
                    int index = current.getIndex();
                    student_reference.child(String.valueOf(index)).addValueEventListener(init_student_keys);
                    if (student_keys_list.containsKey(username)) {
                        int current_student_index = student_keys_list.get(username);
                        student_reference.child(String.valueOf(index)).child(String.valueOf(current_student_index)).removeValue();
                        current.remove_student();
                        reference.child(String.valueOf(index)).child("number_of_students").setValue(current.getNumber_of_students());
                    } else {
                        error_display.setText("You are not enrolled in this course");
                    }
                    return;
                }

            }
        });

        displayCourses.setOnClickListener(new View.OnClickListener() {
            /**displays courses
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(postListener);
            }
        });
        reference.addValueEventListener(postListener);

        course_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_reference.addValueEventListener(init_enrolled_courses);
            }
        });
    }

    /**finds index for a course
     * @author tannergiddings
     * @param course course who's index is to be found
     * @param courseList where it can be found
     * @return the index, if not found, returns -1.
     */
    public int getIndex(Course course, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    /**Verifies if user is registered in a course
     * @author tannergiddings
     * @param username
     * @param studentList
     * @return
     */
    public boolean inCourse(String username, ArrayList<Student> studentList) {
        for (int i = 0; i < studentList.size(); i++) {
            if (username.equals(studentList.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**Finds course in list based on index
     * @author tannergiddings
     * @param index index of course
     * @param courseList list of courses
     * @return course if found. If not, returns null
     */
    public Course findCourse(int index, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getIndex() == index) {
                return courseList.get(i);
            }
        }
        return null;
    }

    private ArrayList<String> findDays(Course course) {
        ArrayList<String> currentCourse = getDays(course);
        int length = 1;
        ArrayList<String> listDays = new ArrayList<>();
        for (int i = 0; i < currentCourse.size(); i++) {
            if (currentCourse.get(i).equals("Monday") || currentCourse.get(i).equals("monday") || currentCourse.get(i).equals("Mondays") || currentCourse.get(i).equals("mondays")) {
                listDays.add("monday");
            } else if (currentCourse.get(i).equals("Tuesday") || currentCourse.get(i).equals("tuesday") || currentCourse.get(i).equals("Tuesdays") || currentCourse.get(i).equals("tuesdays")) {
                listDays.add("tuesday");
            } else if (currentCourse.get(i).equals("Wednesday") || currentCourse.get(i).equals("wednesday") || currentCourse.get(i).equals("Wednesdays") || currentCourse.get(i).equals("wednesdays")) {
                listDays.add("wednesday");
            } else if (currentCourse.get(i).equals("Thurday") || currentCourse.get(i).equals("thursday") || currentCourse.get(i).equals("Thursdays") || currentCourse.get(i).equals("thursdays")) {
                listDays.add("thursday");
            } else if (currentCourse.get(i).equals("Friday") || currentCourse.get(i).equals("friday") || currentCourse.get(i).equals("Fridays") || currentCourse.get(i).equals("fridays")) {
                listDays.add("friday");
            } else if (currentCourse.get(i).equals("Saturday") || currentCourse.get(i).equals("saturday") || currentCourse.get(i).equals("Saturdays") || currentCourse.get(i).equals("saturdays")) {
                listDays.add("saturday");
            } else if (currentCourse.get(i).equals("Sunday") || currentCourse.get(i).equals("sunday") || currentCourse.get(i).equals("Sundays") || currentCourse.get(i).equals("sundays")) {
                listDays.add("sunday");
            }
        }
        return listDays;
    }

    public boolean findDayAmongString(ArrayList<String> days, String day) {
        for (int i = 0; i < days.size(); i++) {
            if (day.equals(days.get(i).trim())) {
                return true;
            }
        }
        return false;
    }

    /**Finds the courses which are offered on a specific day
     * @author tannergiddings
     * @param courses list of courses to be verified
     * @param day day of the week to be used to compare courses
     * @return arraylist of courses offered on a day
     */
    public ArrayList<Course> findDaysAmongCourses(ArrayList<Course> courses, String day) {
        ArrayList<Course> courseList = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            if (findDayAmongString(getDays(courseList.get(i)), day.trim())) {
                courseList.add(courses.get(i));
            }
        }
        return courseList;
    }

    /**Finds a list of courses with the entered code
     * @author tannergiddings
     * @param list arraylist of courses
     * @param code code to compare courses with
     * @return list with courses with the code
     */
    public ArrayList<Course> compareCode(ArrayList<Course> list, String code) {
        ArrayList<Course> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals(code)) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    /**Finds a list of courses with the entered name
     * @author tannergiddings
     * @param list arraylist of courses
     * @param name name to compare courses with
     * @return list with courses with the name
     */
    public ArrayList<Course> compareName(ArrayList<Course> list, String name) {
        ArrayList<Course> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    /**Returns a string of the courses in the arrayList
     * @author tannergiddings
     * @param list arraylist to turn into a string
     * @return a string of the courses
     */
    public String toStringList(ArrayList<Course> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i).toString();
        }
        return str;
    }

    /**Returns a valid index for studentList
     * @author tannergiddings
     * @param student_list arraylist of students
     * @return an index or -1 if error
     */
    public static int createIndexStudents(ArrayList<Student> student_list) {
        if (student_list.size() == 0) {
            return 0;
        } else {
            for (int i = 0; i <= student_list.size(); i++) {
                if (!containsIndex(student_list, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Verifies if an index is contained in any course
     * @param student_list list of courses
     * @param index index to find
     */
    private static boolean containsIndex(ArrayList<Student> student_list, int index) {
        for (int i = 0; i < student_list.size(); i++) {
            if (student_list.get(i).getIndex() == index) {
                return true;
            }
        }
        return false;
    }

    /**Verifies if student is in list of students
     * @author tannergiddings
     * @param student student to look for
     * @param studentList list in which to look for
     * @return returns a boolean value showing if student is in studentList
     */
    public boolean findEnrolmentStudent(ArrayList<String> studentList, String student) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).equals(student)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a list of courses based on a list of course id
     * @param courseList list of all courses
     * @param integer_list list of indices for which we want the courses
     * @return returns the courses who's indices are in integer_list
     */
    public static ArrayList<Course> findCoursesByIds(ArrayList<Integer> integer_list, ArrayList<Course> courseList) {
        ArrayList<Course> new_course_list = new ArrayList<>();
        for (int i = 0; i < courseList.size(); i++) {
            if (integer_list.contains(courseList.get(i).getIndex())) {
                new_course_list.add(courseList.get(i));
            }
        }
        return new_course_list;
    }

    /**
     * Finds a course in a list and returns its index
     * @author tannergiddings
     * @param courseList list of courses
     * @param course course to find
     * @return index of course
     */
    public static int findCourseIndex(ArrayList<Course> courseList, Course course) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    /**
     * Finds a course in a list
     * @author tannergiddings
     * @param courseList list of courses
     * @param course to find
     * @return complete course info
     */
    public Course findCourseInList(ArrayList<Course> courseList, Course course) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).equals(course)) {
                return courseList.get(i);
            }
        }
        return null;
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
     * Finds days in string of times
     * @author tannergiddings
     * @param course entry of course
     * @return days in time
     */
    private ArrayList<String> getDays(Course course) {
        String time = course.getTimes();
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

    private int convertToInt(String entry) {
        String[] entry_splits = entry.split(":");
        int[] int_entry_splits = new int[2];
        for (int i = 0; i < 2; i++) {
            int_entry_splits[i] = convertStringToInt(entry_splits[i]);
            if (int_entry_splits[i] == -1) {
                return -1;
            }
        }
        return int_entry_splits[0]*60 + int_entry_splits[1];

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

    /**
     * swaps integers if one is greater than the other
     */
    public void swap(int smaller, int greater) {
        int swap;
        if (greater < smaller) {
            swap = greater;
            greater = smaller;
            smaller = swap;
        }
    }

    public boolean verify_schedule(Map<String, ArrayList<Integer>> schedule, Course course) {
        String time = course.getTimes();
        HashMap<String, ArrayList<Integer>> new_times = new HashMap<>();
        ArrayList<Integer> new_hours;
        String[] time_split = time.split(",");
        String[] days_and_time;
        String day;
        String[] times;
        String[] hours;
        int start_time = -1;
        int end_time = -1;

        if (course.getDays() == null) {
            return true;
        } else {
            for (int i = 0; i < time_split.length; i++) {
                new_hours = new ArrayList<>();
                days_and_time = time_split[i].split(" ");
                day = days_and_time[0];
                times = days_and_time[1].split("-");
                start_time = convertToInt(times[0]);
                end_time = convertToInt(times[1]);
                if (end_time > 0 && start_time > 0) {
                    if (end_time >= start_time) {
                        new_hours.add(start_time);
                        new_hours.add(end_time);
                    } else {
                        new_hours.add(end_time);
                        new_hours.add(start_time);
                    }
                    new_times.put(day, new_hours);
                }
            }
            ArrayList<Integer> hours1;
            ArrayList<Integer> hours2;
            HashMap<String, ArrayList<Integer>> new_schedule = new HashMap<>();
            for (String current_day : new_times.keySet()) {
                if (!schedule.containsKey(current_day)) {
                    new_schedule.put(current_day, new_times.get(current_day));
                } else {
                    hours1 = new_times.get(current_day);
                    hours2 = schedule.get(current_day);
                    if (hours1.get(0) != null && hours1.get(1) != null && hours2.get(0) != null && hours2.get(1) != null) {
                        if (isOut(hours1.get(0), hours1.get(1), hours2.get(0), hours2.get(1))) {
                            new_schedule.put(current_day, hours1);
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            for (String day_to_be_added : new_schedule.keySet()) {
                schedule.put(day_to_be_added, new_schedule.get(day_to_be_added));
            }
            return true;
        }
    }

    public static boolean isOut(int start_time1, int end_time1, int start_time2, int end_time2) {
        if (start_time1 < start_time2 && start_time1 < end_time2 && end_time1 <= start_time2 && end_time1 < end_time2) {
            return true;
        } else return start_time1 > start_time2 && start_time1 >= end_time2 && end_time1 > start_time2 && end_time1 > end_time2;
    }

}