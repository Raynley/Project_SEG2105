package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class welcome_admin extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText welcome_message;
    ImageView addCourse, editCourse, manageInstructor, manageStudent;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        
        welcome_message =  findViewById(R.id.welcome_message);
        Intent intent = getIntent();
        String name = ("name");
        welcome_message.setText("Welcome "+ name+", you are logged in as an admin");
        
        addCourse = findViewById(R.id.add_course_icon);
        editCourse = findViewById(R.id.edit_course_icon);
        manageInstructor = findViewById(R.id.delete_instruc_icon);
        manageStudent = findViewById(R.id.manage_stud_icon);
        logout = findViewById(R.id.log_out);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_admin.this, MainActivity.class);
                startActivity(intent);
            }
        });
        
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,add_course.class);
                startActivity(intent);
            }
        });

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,edit_courses.class);
                startActivity(intent);
            }
        });

        manageInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,manage_instructors.class);
                startActivity(intent);
            }
        });

        manageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,manage_students.class);
                startActivity(intent);
            }
        });
    }
    
}
