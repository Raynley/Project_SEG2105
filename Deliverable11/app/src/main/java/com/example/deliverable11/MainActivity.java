package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    EditText iname, ipassword;
    Button ilogin;
    TextView icreate;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Admin> adminList;
    ArrayList<Student> studentList;
    ArrayList<Instructor> instructorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      

        iname = findViewById(R.id.userName);
        ipassword = findViewById(R.id.password);
        ilogin = findViewById(R.id.btn_login);
        icreate = findViewById(R.id.textView2);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        adminList = new ArrayList<>();
        studentList = new ArrayList<>();
        instructorList = new ArrayList<>();
        String name = iname.getText().toString();
        String password = ipassword.getText().toString();

        ValueEventListener initLists = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    adminList.clear();
                    for (DataSnapshot ds : snapshot.child("Admin").getChildren()) {
                        adminList.add(ds.getValue(Admin.class));
                    }
                    instructorList.clear();
                    for (DataSnapshot ds : snapshot.child("Instructor").getChildren()) {
                        instructorList.add(ds.getValue(Instructor.class));
                    }
                    studentList.clear();
                    for (DataSnapshot ds : snapshot.child("Student").getChildren()) {
                        studentList.add(ds.getValue(Student.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ilogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = iname.getText().toString();
                String password = ipassword.getText().toString();
                int i;
                Intent intent;
                reference.addValueEventListener(initLists);
                if(nameEmpty( name)){
                    iname.setError("name is required");
                    return;
                } else if(nameEmpty(password)){
                    ipassword.setError("Password is required");
                    return;
                } else {
                    for (i = 0; i < adminList.size(); i++) {
                        if (adminList.get(i).getUsername().equals(name)) {
                            if (adminList.get(i).getPassword().equals(password)) {
                                intent = new Intent(getApplicationContext(),welcome_admin.class);
                                intent.putExtra("USERNAME", name);
                                startActivity(intent);
                            } else {
                                ipassword.setText("Username and password don't match");
                            }
                        }
                    }
                    for (i = 0; i < instructorList.size(); i++) {
                        if (instructorList.get(i).getUsername().equals(name)) {
                            if (instructorList.get(i).getPassword().equals(password)) {
                                intent = new Intent(getApplicationContext(),welcome_instructor.class);
                                intent.putExtra("USERNAME", name);
                                startActivity(intent);
                            } else {
                                ipassword.setText("Username and password don't match");
                            }
                        }
                    }
                    for (i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getUsername().equals(name)) {
                            if (studentList.get(i).getPassword().equals(password)) {
                                startActivity(new Intent(getApplicationContext(), welcome_student.class));
                            } else {
                                ipassword.setText("Username and password don't match");
                            }
                        }
                    }
                }
            }
        });

        icreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
    }
    public static boolean nameEmpty(String name){
        if(name.length()==0){
            return true;
        }
        return false;
    }
}
