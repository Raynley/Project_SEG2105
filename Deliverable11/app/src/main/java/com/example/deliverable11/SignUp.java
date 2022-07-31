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

/**Allows a new user to sign up as an instructor or a student
 * @author Moumin Farah
 */
public class SignUp extends AppCompatActivity {
    EditText iname, ipassword, irepassword;
    Button iregister;
    TextView ilogin;
    FirebaseDatabase rootCourse;
    DatabaseReference reference;
    RadioButton instructorBtn, studentBtn;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign up");

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
            /**initialises list of new students
             * @author tannergiddings
             * @param snapshot snapshot of database
             */
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
            /**initialises instructorList
             * @author tannergiddings
             * @param snapshot snapshot of database
             */
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
            /**changes 'type' to "instructor"
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                type = "instructor";
            }
        });

        studentBtn.setOnClickListener(new View.OnClickListener() {
            /**changes 'type' to "student"
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                type = "student";
            }
        });

        iregister.setOnClickListener(new View.OnClickListener() {
            /**Verifies if it's a proper user, creates it and adds it to the database
             * @author Moumin Farah
             * @param v
             */
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
                        student.setIndex(studentList.size());
                        reference.child(String.valueOf(createIndexStudent(studentList))).setValue(student);
                        iname.setText("");
                        ipassword.setText("");
                        irepassword.setText("");
                    } else if (type.equals("instructor")) {
                        Instructor instructor = new Instructor(name, password);
                        reference = rootCourse.getReference("User").child("Instructor");
                        reference.addValueEventListener(initInstructorList);
                        instructor.setIndex(instructorList.size());
                        reference.child(String.valueOf(createIndexInstructor(instructorList))).setValue(instructor);
                        iname.setText("");
                        ipassword.setText("");
                        irepassword.setText("");
                    }
                }
            }
        });

        ilogin.setOnClickListener(new View.OnClickListener() {
            /**Brings the user back to MainActivity
             * @author Moumin Farah
             * @param v
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    /**Verifies if a string is of correct length
     * @author Laure
     * @param x entry to be verified
     * @return false if length of string is greater than 6, true otherwise
     */
    public static Boolean isCorrectLength(String x){
        if (x.length()<6){
            return false;
        }
        return true;
    }

    /**Verifies if two string equals eachother
     * @author Laure
     * @param x First string to compare
     * @param y Second string to compare
     * @return True if they are equals. False otherwise
     */
    public static Boolean passwordMatch(String x, String y){
        if(!x.equals(y)){
            return false;
        }
        return true;
    }

    /**Returns a valid index for student's lists
     * @author tannergiddings
     * @param student_list arraylist of users
     * @return an index or -1 if error
     */
    public int createIndexStudent(ArrayList<Student> student_list) {
        if (student_list.size() == 0) {
            return 0;
        } else {
            for (int i = 0; i <= student_list.size(); i++) {
                if (!containsIndexStudent(student_list, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**Returns a valid index for instructor's lists
     * @author tannergiddings
     * @param instructor_list arraylist of users
     * @return an index or -1 if error
     */
    public int createIndexInstructor(ArrayList<Instructor> instructor_list) {
        if (instructor_list.size() == 0) {
            return 0;
        } else {
            for (int i = 0; i <= instructor_list.size(); i++) {
                if (!containsIndexInstructor(instructor_list, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Verifies if an index is contained for any student
     * @param student_list list of courses
     * @param index index to find
     */
    private boolean containsIndexStudent(ArrayList<Student> student_list, int index) {
        for (int i = 0; i < student_list.size(); i++) {
            if (student_list.get(i).getIndex() == index) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if an index is contained for any instructor
     * @param student_list list of courses
     * @param index index to find
     */
    private boolean containsIndexInstructor(ArrayList<Instructor> student_list, int index) {
        for (int i = 0; i < student_list.size(); i++) {
            if (student_list.get(i).getIndex() == index) {
                return true;
            }
        }
        return false;
    }
}
