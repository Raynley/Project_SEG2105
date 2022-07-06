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

public class welcome_instructor extends AppCompatActivity {
    EditText name_entry, code_entry, new_days, new_hours, new_capacity, new_description;
    TextView displayCourses, error_display;
    ImageButton add_btn, remove_btn, edit_btn;
    FirebaseDatabase database;
    DatabaseReference reference;
    //https://stackoverflow.com/questions/47779081/retrieve-information-from-firebase-element-clicked-in-listview


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
        error_display = findViewById(R.id.Error);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        ArrayList<Course> courseList = new ArrayList<Course>();

        ValueEventListener postListener = new ValueEventListener() {
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
                for (int i = 0; i < courseList.size(); i++) {
                    if (courseList.get(i).equals(newCourse)) {
                        /*
                        We have to find a way to make it so that the
                        username they enter in the login page is kept until
                        this step for it to be used below.
                        FIXED
                         */
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
                        if (newCourse.setInstructor(username)) {
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
                            error_display.setText("Course already has an instructor assigned to it");
                        }
                    } else {
                        error_display.setText("Course was not found");
                        name_entry.setText("");
                        code_entry.setText("");
                        new_days.setText("");
                        new_hours.setText("");
                        new_capacity.setText("");
                        new_description.setText("");
                    }
                }
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                String days = new_days.getText().toString();
                String hours = new_hours.getText().toString();
                String capacityString = new_capacity.getText().toString();
                int capacity =-1;

                if(!isValidCapacity(capacityString)) {
                    new_capacity.setText("You must enter a number");
                    return;
                }
                else{
                    capacity = Integer.parseInt(name);
                }

                String description = new_description.getText().toString();
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
                            database.getReference("Courses").child(name).removeValue();
                            if (!TextUtils.isEmpty(days)) {
                                newCourse.setDays(days);
                            }
                            if (!TextUtils.isEmpty(hours)) {
                                newCourse.setHours(hours);
                            }
                            if (capacity > 0) {
                                newCourse.setCourse_capacity(capacity);
                            }
                            if (!TextUtils.isEmpty(description)) {
                                newCourse.setDescription(description);
                            }
                            reference.child(name).setValue(newCourse);
                            name_entry.setText("");
                            code_entry.setText("");
                            new_days.setText("");
                            new_hours.setText("");
                            new_capacity.setText("");
                            new_description.setText("");
                            error_display.setText("");
                        }
                    }
                }
            }
        });

        remove_btn.setOnClickListener(new View.OnClickListener() {
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
                            if (newCourse.getInstructor().equals(username)) {
                                newCourse.removeInstructor();
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
            }
        });
        reference.addValueEventListener(postListener);


    }

    public static Boolean isValidCapacity(String name){
        int capacity = -1;
        try {
            capacity = Integer.parseInt(name);
            return true ;

        } catch (NumberFormatException e) {
            return false;
        }

    }
}