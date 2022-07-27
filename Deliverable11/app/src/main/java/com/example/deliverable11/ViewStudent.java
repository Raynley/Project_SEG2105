package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewStudent extends AppCompatActivity {
    EditText course_name, course_code;
    Button find_students, go_back;
    TextView view_students;
    ArrayList<Course> courseList;
    FirebaseDatabase database;
    DatabaseReference reference, student_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        course_name = findViewById(R.id.course_code_info);
        course_code = findViewById(R.id.course_name);
        find_students = findViewById(R.id.find_students);
        view_students = findViewById(R.id.view_students);
        go_back = findViewById(R.id.back_to_welcome_instructor);
        courseList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");

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

        ValueEventListener display_students = new ValueEventListener() {
            /**Initialises list of students
             * @author tannergiddings
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String str = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        str = str + ds.getValue(Student.class).getUsername() + "\n";
                    }
                    view_students.setText(str);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        find_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(initList);
                String name = course_name.getText().toString().trim();
                String code = course_code.getText().toString().trim();

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(code))  {
                    course_name.setError("Course name is required");
                    course_code.setError("Course code is required");
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    course_name.setError("Course name is required");
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    course_code.setError("Course code is required");
                    return;
                } else {
                    int index = findIndex(new Course(name, code), courseList);

                    if (index == -1) {
                        view_students.setText("Course was not found");
                        return;
                    } else {
                        student_reference = database.getReference("Students").child(String.valueOf(index));
                        student_reference.addValueEventListener(display_students);
                    }
                }
            }
        });

        view_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_students.setText("");
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewStudent.this, welcome_instructor.class);
                startActivity(intent);
            }
        });
    }

    public int findIndex(Course course, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }
}