package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**Methods and functionnalities for welcome_admin.xml
 * Allows admin to go to add_course, edit_course, manage_instructors and manage_students
 * @author tannergiddings
 */
public class welcome_admin extends AppCompatActivity {
    FirebaseAuth fAuth;
    TextView welcome_message;
    ImageView addCourse, editCourse, manageInstructor, manageStudent;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        this.setTitle("Welcome admin");
        
        welcome_message =  findViewById(R.id.welcome_message);
        Intent intent = getIntent();
        String name = ("name");
        welcome_message.setText("Welcome "+ name+", you are logged in as an admin");
        
        addCourse = findViewById(R.id.add_course_icon);
        editCourse = findViewById(R.id.edit_course_icon);
        manageInstructor = findViewById(R.id.delete_instruc_icon);
        manageStudent = findViewById(R.id.manage_stud_icon);
        logout = findViewById(R.id.log_out);

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

        welcome_message.setText("Welcome " + username + ", you are logged in as an admin.");

        logout.setOnClickListener(new View.OnClickListener() {
            /**Goes back to MainActivity
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_admin.this, MainActivity.class);
                startActivity(intent);
            }
        });
        
        addCourse.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes to add_course
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_admin.this,add_course.class);
                startActivity(intent);
            }
        });

        editCourse.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes to edit_course
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_admin.this,edit_courses.class);
                startActivity(intent);
            }
        });

        manageInstructor.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes to manage_instructors
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_admin.this,manage_instructors.class);
                startActivity(intent);
            }
        });

        manageStudent.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes to manage_students
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome_admin.this,manage_students.class);
                startActivity(intent);
            }
        });
    }
    
}
