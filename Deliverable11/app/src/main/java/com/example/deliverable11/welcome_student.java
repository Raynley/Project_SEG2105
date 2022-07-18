package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
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

public class welcome_student extends AppCompatActivity {
    EditText name_entry, code_entry;
    ImageButton add_btn, remove_btn, search_btn;
    TextView displayCourses, error_display, course_view;
    ArrayList<Course> courseList;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_student);

        name_entry = findViewById(R.id.course_name_to_edit);
        code_entry = findViewById(R.id.course_code_to_edit);
        add_btn = findViewById(R.id.add_btn);
        remove_btn = findViewById(R.id.delete_course_btn);
        search_btn = findViewById(R.id.search_btn);
        displayCourses = findViewById(R.id.displayCourse);
        error_display = findViewById(R.id.Error);
        course_view = findViewById(R.id.Enrol_View);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        ArrayList<Course> courseList = new ArrayList<>();
        ArrayList<Course> enrolled_courses = new ArrayList<>();
        ArrayList<String> students = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        students.add(ds.getValue(Student.class).getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener init_enrolled_courses = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course current;
                    String textDisplay = "";
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
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        current = ds.getValue(Course.class);
                        for (DataSnapshot ds_student : ds.child("Students").getChildren()) {
                            if (ds_student.getValue(Student.class).getUsername().equals(username)) {
                                enrolled_courses.add(current);
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < enrolled_courses.size(); i++) {
                        textDisplay = textDisplay + enrolled_courses.get(i).basicToString();
                    }
                    course_view.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener initList = new ValueEventListener() {
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
        reference.addValueEventListener(init_enrolled_courses);
        /*
        Having trouble adding student to database.
        Have to verify if you can add ArrayLists to databases.
        Possible changes required.
         */
        add_btn.setOnClickListener(new View.OnClickListener() {
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
                reference.addValueEventListener(initList);
                int index = getIndex(new Course(name, code), courseList);
                if (index < 0) {
                    error_display.setText("Course not found");
                    return;
                } else {
                    reference.child(String.valueOf(index)).addValueEventListener(init_students);
                    boolean inCourse = inCourse(username, students);
                    if (inCourse) {
                        error_display.setText("You are already enrolled in this course");
                        return;
                    } else {
                        Course current = findCourse(index, courseList);
                        if (current.addStudent()) {
                            reference.child(String.valueOf(index)).child("course_capacity").setValue(current.getCourse_capacity());
                            reference.child(String.valueOf(index)).child("Students").child(String.valueOf(students.size())).setValue(username);
                            error_display.setText("");
                            name_entry.setText("");
                            code_entry.setText("");
                        } else {
                            error_display.setText("Course at capacity. Enrolment failed");
                        }
                    }
                }
            }
        });
        /*
        Need to add remove button functionalities.
        Will need to add a remove method to the course class.
        Will need to implement weird stuff to database.
         */
        search_btn.setOnClickListener(new View.OnClickListener() {
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
                    error_display.setText("");
                    displayCourses.setText(display);
                } else if (!TextUtils.isEmpty(name)) {
                    for (i = 0; i < courseList.size(); i++) {
                        current = courseList.get(i);
                        if (current.getName().equals(name)) {
                            display = display + current.toString() + "\n";
                        }
                    }
                    error_display.setText("");
                    displayCourses.setText(display);
                } else {
                    for (i = 0; i < courseList.size(); i++) {
                        current = courseList.get(i);
                        if (current.getName().equals(name) && current.getCode().equals(code)) {
                            display = display + current.toString() + "\n";
                        }
                    }
                    error_display.setText("");
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

    public int getIndex(Course course, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    public boolean inCourse(String username, ArrayList<String> studentList) {
        for (int i = 0; i < studentList.size(); i++) {
            if (username.equals(studentList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public Course findCourse(int index, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getIndex() == index) {
                return courseList.get(i);
            }
        }
        return null;
    }

    public String[] findDays(String days) {

    }
}