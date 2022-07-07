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

public class welcome_student extends AppCompatActivity {
    EditText name_entry, code_entry;
    ImageButton add_btn, remove_btn, search_btn;
    TextView displayCourses, error_display;
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
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        ArrayList<Course> courseList = new ArrayList<>();

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
                    Course current = courseList.get(i);
                    if (current.equals(newCourse)) {
                        newCourse = current;
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
                        if (newCourse.addStudent(username)) {
                            reference.child(name).removeValue();
                            reference.child(name).setValue(newCourse);
                            name_entry.setText("");
                            code_entry.setText("");
                            break;
                        } else {
                            error_display.setText("Course capacity full. Enrolment failed");
                            name_entry.setText("");
                            code_entry.setText("");
                            break;
                        }
                    }
                }
            }
        });
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
}