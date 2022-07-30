package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;

/**Allows instructor to add themselves to a course. Change the details of it and remove
 * themselves from a course
 * @author tannergiddings
 */
public class welcome_instructor extends AppCompatActivity {
    EditText name_entry, code_entry, new_days, new_capacity, new_description;
    TextView displayCourses, error_display;
    ImageButton add_btn, remove_btn, edit_btn, search_btn;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button viewStudentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_instructor);
        this.setTitle("Welcome Instructor");

        name_entry = findViewById(R.id.course_name_to_edit_inst);
        code_entry = findViewById(R.id.course_code_to_edit_inst);
        new_days = findViewById(R.id.course_days);
        new_capacity = findViewById(R.id.course_capacity);
        new_description = findViewById(R.id.course_description);
        displayCourses = findViewById(R.id.displayCourse_inst);
        add_btn = findViewById(R.id.add_btn);
        remove_btn = findViewById(R.id.delete_course_btn_inst);
        edit_btn = findViewById(R.id.edit_course_btn_inst);
        search_btn = findViewById(R.id.search_btn);
        error_display = findViewById(R.id.Error);
        viewStudentButton = findViewById((R.id.view_student_btn));
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        ArrayList<Course> courseList = new ArrayList<Course>();

        ValueEventListener postListener = new ValueEventListener() {
            /**
             * Displays the courses to the instructor
             * @author tannergiddings
             * @param snapshot snapshot of database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Course.class).toString() + "\n";
                    }
                    displayCourses.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        ValueEventListener initList = new ValueEventListener() {
            /**
             * Initialises courseList
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

        reference.addValueEventListener(postListener);

        add_btn.setOnClickListener(new View.OnClickListener() {
            /**
             * Adds instructor to course if there isn't already an instructor assigned to it
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
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

                reference.addValueEventListener(initList);
                Course newCourse = new Course(name, code);
                int index = getIndex(newCourse, courseList);

                if (index < 0) {
                    error_display.setText("Course was not found");
                    name_entry.setText("");
                    code_entry.setText("");
                    new_days.setText("");
                    new_capacity.setText("");
                    new_description.setText("");
                } else {
                    String username;
                    newCourse = findCourse(index, courseList);

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

                    if (!newCourse.getHasInstructor()) {
                        newCourse.setInstructor(username);
                        reference.child(String.valueOf(index)).setValue(newCourse);
                        name_entry.setText("");
                        code_entry.setText("");
                        new_days.setText("");
                        new_capacity.setText("");
                        new_description.setText("");
                        error_display.setText("");
                    } else {
                        error_display.setText("Course already has an instructor assigned to it");
                    }
                }
                reference.addValueEventListener(postListener);
            }
        });

        viewStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                Intent intent = new Intent(getApplicationContext(), ViewStudent.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            /**
             * Allows instructor to edit a course's details
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                error_display.setText("");
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                String times = new_days.getText().toString();
                String capacityString = new_capacity.getText().toString();
                String description = new_description.getText().toString();
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
                int capacity = 0;
                if (!TextUtils.isEmpty(capacityString)) {
                    try {
                        capacity = Integer.parseInt(capacityString);
                    } catch (NumberFormatException e) {
                        new_capacity.setText("You must enter a number");
                        return;
                    }
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
                } else if (TextUtils.isEmpty(times) && TextUtils.isEmpty(capacityString)
                        && TextUtils.isEmpty(description)) {
                    error_display.setText("Information to edit missing");
                } else {
                    reference.addValueEventListener(initList);
                    Course newCourse = new Course(name, code);
                    int index = getIndex(newCourse, courseList);

                    if (index < 0) {
                        error_display.setText("Course was not found");
                        name_entry.setText("");
                        code_entry.setText("");
                        new_days.setText("");
                        new_capacity.setText("");
                        new_description.setText("");
                    } else {
                        newCourse = findCourse(index, courseList);
                        if (newCourse.getInstructor().equals(username)) {
                            if (!TextUtils.isEmpty(times)) {
                                if (valid_time_entry(times)) {
                                    newCourse.setTimes(times);
                                    reference.child(String.valueOf(index)).setValue(newCourse);
                                } else {
                                    new_days.setText("Invalid days entered");
                                }
                            }

                            if (!TextUtils.isEmpty(capacityString)) {
                                if (isValidCapacity(capacityString)) {
                                    newCourse.setCourse_capacity(capacity);
                                    reference.child(String.valueOf(index)).setValue(newCourse);
                                } else {
                                    new_capacity.setText("Invalid capacity entered");
                                }
                            }
                            if (!TextUtils.isEmpty(description)) {
                                if (isValidDescription(description)) {
                                    newCourse.setDescription(description);
                                    reference.child(String.valueOf(index)).setValue(newCourse);
                                } else {
                                    new_description.setText("Invalid description entered");
                                }
                            }
                            name_entry.setText("");
                            code_entry.setText("");
                            new_days.setText("");
                            new_capacity.setText("");
                            new_description.setText("");
                            error_display.setText("");
                        } else {
                            error_display.setText("You are not the instructor for this course");
                        }
                    }

                }
                reference.addValueEventListener(postListener);
            }
        });

        remove_btn.setOnClickListener(new View.OnClickListener() {
            /**Allows an instructor to remove themselves from a course
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
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
                } else {
                    reference.addValueEventListener(initList);
                    Course newCourse = new Course(name, code);
                    for (int i = 0; i < courseList.size(); i++) {
                        if (courseList.get(i).equals(newCourse)) {
                            newCourse = courseList.get(i);
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
                            if (reference.child(String.valueOf(newCourse.getIndex())).child("username").equals(username)) {
                                newCourse = new Course(name, code);
                                reference.child(name).removeValue();
                                reference.child(name).setValue(newCourse);
                                name_entry.setText("");
                                code_entry.setText("");
                                new_days.setText("");
                                new_capacity.setText("");
                                new_description.setText("");
                                error_display.setText("");
                            } else {
                                error_display.setText("You are not the instructor for this course");
                            }
                        } else {
                            error_display.setText("Course wasn't found");
                        }
                    }
                }
                reference.addValueEventListener(postListener);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            /**Allows instructor to search for a course based on name and code
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(initList);
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                int i;
                String display = "";
                Course current;
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(code)) {
                    name_entry.setText("A value is needed");
                    return;
                } else if (!TextUtils.isEmpty(code)) {
                    for (i = 0; i < courseList.size(); i++) {
                        current = courseList.get(i);
                        if (current.getCode().equals(code)) {
                            display = display + current.toString() + "\n";
                        }
                    }
                    displayCourses.setText(display);
                } else if (!TextUtils.isEmpty(name)) {
                    for (i = 0; i < courseList.size(); i++) {
                        current = courseList.get(i);
                        if (current.getName().equals(name)) {
                            display = display + current.toString() + "\n";
                        }
                    }
                    displayCourses.setText(display);
                } else {
                    for (i = 0; i < courseList.size(); i++) {
                        current = courseList.get(i);
                        if (current.getName().equals(name) && current.getCode().equals(code)) {
                            display = display + current.toString() + "\n";
                        }
                    }
                    displayCourses.setText(display);
                }
            }
        });

        displayCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(postListener);
            }
        });
    }

    /**Verifies if the entry is a valid capacity. i.e is a positive integer
     * @author tannergiddings
     * @param name capacity to be verified
     * @return if it is a valid capacity
     */
    public static Boolean isValidCapacity(String name){
        int capacity = -1;
        try {
            capacity = Integer.parseInt(name);
            return true ;

        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**Verifies if the entry is a day of the week
     * @author tannergiddings
     * @param day entry to be verified
     * @return if the entry is a day or not
     */
    private boolean isDayOfWeek(String day) {
        day = day.trim();
        if (day.equals("Monday") || day.equals("monday") || day.equals("Tuesday") || day.equals("tuesday")
                || day.equals("Wednesday") || day.equals("wednesday") || day.equals("Thursday") ||
                day.equals("thursday") || day.equals("Friday") || day.equals("friday") || day.equals("Saturday")
                || day.equals("saturday") || day.equals("Sunday") || day.equals("sunday")) {
            return true;
        } else {
            return false;
        }
    }

    /**Verifies if the entry is a valid description
     * @author tannergiddings
     * @param description entry to be verified
     * @return if it's a valid description
     */
    public boolean isValidDescription(String description) {
        return description instanceof String;
    }

    /**Finds index for a course
     * @author tannergiddings
     * @param course course who's index is to be found
     * @param courseList arraylist in which the index can be found
     * @return index of course if found. If not, it returns -1.
     */
    public int getIndex(Course course, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    /**Finds the instructor for a course in specific
     * @author tannergiddings
     * @param index index of the course
     * @param courseList list of courses
     */
    public String findInstructor(int index, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getIndex() == index) {
                if (courseList.get(i).getHasInstructor()) {
                    return courseList.get(i).getInstructor();
                } else {
                    return "";
                }
            }
        }
        return "";
    }

    /**Finds a course in courseList with index
     * @param index index of the course
     * @param courseList course list
     * @return course with that index
     */
    public Course findCourse(int index, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getIndex() == index) {
                return courseList.get(i);
            }
        }
        return null;
    }

    /**
     * Verifies if it is a valid time entry
     * @param entry entry to be verified
     * @return if it is valid
     */
    public boolean valid_time_entry(String entry) {
        String[] times = entry.split(",");
        for (int i = 0; i < times.length; i++) {
            if (!isValidTime(times[i])) {
                return false;
            }
        }
        return true;
        /*
        String[] split_entry = entry.split("-");
        for (int i = 0; i < split_entry.length; i++) {
            if (!isValidHours(split_entry[i]) && isValidDays(split_entry[i])) {
                return false;
            }
        }
        return true;
         */
    }

    private boolean isValidTime(String entry) {
        String[] times = entry.split(" ");
        return isDayOfWeek(times[0]) && isValidHourSpread(times[1]);
    }

    private boolean isValidHourSpread(String entry) {
        String[] times = entry.split("-");
        return isValidHour(times[0]) && isValidHour(times[1]);
    }

    private boolean isValidHour(String entry) {
        String[] times = entry.split(":");
        int hour = convertStringToInt(times[0]);
        int minute = convertStringToInt(times[1]);
        return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
    }



    /**
     * Converts time entry to a map
     * @param entry String entry of time
     * @return map of the times
     */
    public HashMap<String, ArrayList<Integer>> convertToMap(String entry) {
        String[] entry_splits = entry.split(" ");
        String[] current_split;
        ArrayList<Integer> new_times;
        int time1 = -1;
        int time2 = -1;
        String day = null;
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();

        for (int i = 0; i < entry_splits.length; i++) {
            current_split = entry_splits[i].split("-");
            new_times = new ArrayList<>();
            for (int j = 0; j < current_split.length; j++) {
                if (isDayOfWeek(current_split[j].trim())) {
                    day = current_split[j];
                } else if (convertToInt(current_split[j].trim()) != -1) {
                    if (time1 == -1) {
                        time1 = convertToInt(current_split[j].trim());
                    } else if (time2 == -1) {
                        time2 = convertToInt(current_split[j].trim());
                    }
                }
            }
            if (day != null && time1 != -1 && time2 != 1) {
                new_times.add(time1);
                new_times.add(time2);
                map.put(day, new_times);
            }
        }
        return map;
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
    public int convertStringToInt(String entry) {
        try {
            return Integer.parseInt(entry.trim());
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}
