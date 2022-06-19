package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCourseName = course_name.getText().toString().trim();
                String newCourseCode = course_code.getText().toString().trim();

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

                    reference.child(name).setValue(course);
                    course_name.setText("");
                    course_code.setText("");
                }
            }
        });

    }


}
