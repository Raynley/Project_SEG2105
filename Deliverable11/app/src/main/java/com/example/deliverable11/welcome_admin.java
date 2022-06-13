package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class welcome_admin extends AppCompatActivity {
    FirebaseAuth fAuth;
    ImageView addCourse, editCourse, manageInstructor, manageStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        
        addCourse = findViewById(R.id.add_course_icon);
        editCourse = findViewById(R.id.edit_course_icon);
        manageInstructor = findViewById(R.id.delete_instruc_icon);
        manageStudent = findViewById(R.id.manage_stud_icon);
        
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,/*ENTER NAME OF FILE*/);
                startActivity(intent);
            }
        });

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,/*ENTER NAME OF FILE*/);
                startActivity(intent);
            }
        });

        manageInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,/*ENTER NAME OF FILE*/);
                startActivity(intent);
            }
        });

        manageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO COMPLETE
                Intent intent = new Intent(welcome_admin.this,/*ENTER NAME OF FILE*/);
                startActivity(intent);
            }
        });
    }
    
}
