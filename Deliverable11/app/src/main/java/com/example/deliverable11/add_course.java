package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class add_course extends AppCompatActivity {
    EditText course_name, course_code;
    Button course_btn;

    FirebaseDatabase rootCourse;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        course_name = findViewById(R.id.create_course_name);
        course_code = findViewById(R.id.create_course_code);
        course_btn = findViewById(R.id.create_course_btn);
        ArrayList<Course> course_list = new ArrayList<>();

        ValueEventListener initList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    course_list.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        course_list.add(ds.getValue(Course.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCourseName = course_name.getText().toString().trim();
                String newCourseCode = course_code.getText().toString().trim();
                reference.addValueEventListener(initList);

                if (TextUtils.isEmpty(newCourseName) && TextUtils.isEmpty(newCourseCode))  {
                    course_name.setError("Course name is required");
                    course_code.setError("Course code is required");
                    return;
                } else if (TextUtils.isEmpty(newCourseName)) {
                    course_name.setError("Course name is required");
                    return;
                } else if (TextUtils.isEmpty(newCourseCode)) {
                    course_code.setError("Course code is required");
                    return;
                } else {
                    //ENTER THE NEW COURSE INTO THE DATABASE
                    rootCourse = FirebaseDatabase.getInstance();
                    reference = rootCourse.getReference("Courses");

                    String name = course_name.getEditableText().toString();
                    String code = course_code.getEditableText().toString();

                    Course course = new Course(name, code);

                    reference.child(String.valueOf(course_list.size())).setValue(course);
                    course_name.setText("");
                    course_code.setText("");
                }
            }
        });

    }


}
