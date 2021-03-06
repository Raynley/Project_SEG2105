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

/**Allows instructor to add themselves to a course. Change the details of it and remove
 * themselves from a course
 * @author tannergiddings
 */
public class welcome_instructor extends AppCompatActivity {
    EditText name_entry, code_entry, new_days, new_hours, new_capacity, new_description;
    TextView displayCourses, error_display;
    ImageButton add_btn, remove_btn, edit_btn, search_btn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_instructor);

        name_entry = findViewById(R.id.course_name_to_edit_inst);
        code_entry = findViewById(R.id.course_code_to_edit_inst);
        new_days = findViewById(R.id.course_days);
        new_hours = findViewById(R.id.course_hours);
        new_capacity = findViewById(R.id.course_capacity);
        new_description = findViewById(R.id.course_description);
        displayCourses = findViewById(R.id.displayCourse_inst);
        add_btn = findViewById(R.id.add_btn);
        remove_btn = findViewById(R.id.delete_course_btn_inst);
        edit_btn = findViewById(R.id.edit_course_btn_inst);
        search_btn = findViewById(R.id.search_btn);
        error_display = findViewById(R.id.Error);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        ArrayList<Course> courseList = new ArrayList<Course>();

        ValueEventListener postListener = new ValueEventListener() {
            /**Displays the courses to the instructor
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

        reference.addValueEventListener(postListener);

        add_btn.setOnClickListener(new View.OnClickListener() {
            /**Adds instructor to course if there isn't already an instructor assigned to it
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
                    new_hours.setText("");
                    new_capacity.setText("");
                    new_description.setText("");
                } else {
                    String username;
                    newCourse = courseList.get(index);
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
                        reference.child(String.valueOf(index)).child("instructor").setValue(username);
                        name_entry.setText("");
                        code_entry.setText("");
                        new_days.setText("");
                        new_hours.setText("");
                        new_capacity.setText("");
                        new_description.setText("");
                        error_display.setText("");
                        return;
                    } else {
                        error_display.setText("Course already has an instructor assigned to it");
                        return;
                    }
                }
                reference.addValueEventListener(postListener);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            /**Allows instructor to edit a course's details
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                String days = new_days.getText().toString();
                String hours = new_hours.getText().toString();
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
                } else if (TextUtils.isEmpty(days) && TextUtils.isEmpty(hours) && TextUtils.isEmpty(capacityString)
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
                        new_hours.setText("");
                        new_capacity.setText("");
                        new_description.setText("");
                    } else {
                        newCourse = courseList.get(index);
                        if (newCourse.getInstructor().equals(username)) {
                            if (!TextUtils.isEmpty(days)) {
                                if (isValidDays(days)) {
                                    reference.child(String.valueOf(index)).child("days").setValue(days);
                                } else {
                                    new_days.setText("Invalid days entered");
                                }
                            }
                            if (!TextUtils.isEmpty(hours)) {
                                if (isValidHours(hours)) {
                                    reference.child(String.valueOf(index)).child("hours").setValue(hours);
                                } else {
                                    new_hours.setText("Invalid hours entered");
                                }
                            }
                            if (!TextUtils.isEmpty(capacityString)) {
                                if (isValidCapacity(capacityString)) {
                                    reference.child(String.valueOf(index)).child("course_capacity").setValue(capacity);
                                } else {
                                    new_capacity.setText("Invalid capacity entered");
                                }
                            }
                            if (!TextUtils.isEmpty(description)) {
                                if (isValidDescription(description)) {
                                    reference.child(String.valueOf(index)).child("description").setValue(description);
                                } else {
                                    new_description.setText("Invalid description entered");
                                }
                            }
                            name_entry.setText("");
                            code_entry.setText("");
                            new_days.setText("");
                            new_hours.setText("");
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
                            if (/*newCourse.getInstructor().equals(username)*/
                            reference.child(String.valueOf(newCourse.getIndex())).child("username").equals(username)) {
                                newCourse = new Course(name, code);
                                reference.child(name).removeValue();
                                reference.child(name).setValue(newCourse);
                                name_entry.setText("");
                                code_entry.setText("");
                                new_days.setText("");
                                new_hours.setText("");
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
        reference.addValueEventListener(postListener);
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

    /**Verifies if days is a valid string of days of the week
     * @author tannergiddings
     * @param days entry to be verified
     * @return if it is valid
     */
    public boolean isValidDays(String days) {
        String[] list_days = days.split(",");
        boolean verification = true;
        for (int i = 0; i < list_days.length; i++) {
            verification = verification && isDayOfWeek(list_days[i]);
            if (!verification) {
                break;
            }
        }
        return verification;
    }

    /**Verifies if the entry is a valid hour
     * @author tannergiddings
     * @param hour entry to be verified
     * @return if it is valid
     */
    private boolean isHour(String hour) {
        String[] hourNumbers = hour.split(":");
        boolean verification = true;
        int numb;
        for (int i = 0; i < hourNumbers.length; i++) {
            try {
                numb = Integer.parseInt(hourNumbers[i]);
            } catch (NumberFormatException e) {
                verification = false;
                break;
            }
        }
        return verification;
    }

    /**Verifies if the entry is a valid time
     * @author tannergiddings
     * @param time entry to be verified
     * @return if it is a valid time
     */
    private boolean isValidTime(String time) {
        String[] time_list = time.split("-");
        boolean verification = true;
        for (int i = 0; i < time_list.length; i++) {
            verification = isHour(time_list[i]);
            if (!verification) {
                break;
            }
        }
        return verification;
    }

    /**Verifies if the entry is valid for hours of the course
     * @author tannergiddings
     * @param hours entry to be verified
     * @return if it is valid
     */
    public boolean isValidHours(String hours) {
        String[] hours_list = hours.split(" ");
        for (int i = 0; i < hours_list.length; i++) {
            if (!isValidTime(hours_list[i])) {
                return false;
            }
        }
        return true;
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
}
