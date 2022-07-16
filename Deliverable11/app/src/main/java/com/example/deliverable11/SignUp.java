package com.example.deliverable11;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    EditText iname, ipassword, irepassword;
    Button iregister;
    TextView ilogin;
    //FirebaseAuth fAuth;
    FirebaseDatabase rootCourse;
    DatabaseReference reference;
    RadioButton instructorBtn, studentBtn;
    //ProgressBar iprogressbar;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iname = findViewById(R.id.userName);
        rootCourse = FirebaseDatabase.getInstance();
        ipassword = findViewById(R.id.password);
        irepassword = findViewById(R.id.reenter_password);
        iregister = findViewById(R.id.create_the_account);
        ilogin = findViewById(R.id.login);
        instructorBtn = findViewById(R.id.instructor_button);
        studentBtn = findViewById(R.id.student_button);
        ArrayList<Student> studentList = new ArrayList<>();
        ArrayList<Instructor> instructorList = new ArrayList<>();
        type = "";

        ValueEventListener initStudentList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    studentList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        studentList.add(ds.getValue(Student.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener initInstructorList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    instructorList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        instructorList.add(ds.getValue(Instructor.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        instructorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "instructor";
            }
        });

        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "student";
            }
        });

        iregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = iname.getText().toString().trim();
                String password = ipassword.getText().toString().trim();
                String rePassword = irepassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    iname.setError("name is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    ipassword.setError("Password is required");
                    return;
                } else if (!isCorrectLength(password)) {
                    ipassword.setError("must contain 6+ characters");
                    return;
                } else if (!passwordMatch(password,rePassword)){
                    irepassword.setError("Both passwords aren't the same");
                } else {
                    if (type.equals("student")) {
                        Student student = new Student(name, password);
                        reference = rootCourse.getReference("User").child("Student");
                        reference.addValueEventListener(initStudentList);
                        reference.child(String.valueOf(studentList.size())).setValue(student);
                        iname.setText("");
                        ipassword.setText("");
                        irepassword.setText("");
                    } else if (type.equals("instructor")) {
                        Instructor instructor = new Instructor(name, password);
                        reference = rootCourse.getReference("User").child("Instructor");
                        reference.addValueEventListener(initInstructorList);
                        reference.child(String.valueOf(instructorList.size())).setValue(instructor);
                        iname.setText("");
                        ipassword.setText("");
                        irepassword.setText("");
                    }
                }
            }
        });

        ilogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
    public static Boolean isCorrectLength(String x){
        if (x.length()<6){
            return false;
        }
        return true;
    }

    public static Boolean passwordMatch(String x, String y){
        if(!x.equals(y)){
            return false;
        }
        return true;
    }
}
