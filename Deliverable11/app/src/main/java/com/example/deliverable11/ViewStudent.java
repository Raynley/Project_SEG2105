package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
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
import java.util.HashMap;
import java.util.Map;

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
        this.setTitle("View Students");

        course_name = findViewById(R.id.course_code_info);
        course_code = findViewById(R.id.course_name);
        find_students = findViewById(R.id.find_students);
        view_students = findViewById(R.id.view_students);
        go_back = findViewById(R.id.back_to_welcome_instructor);
        courseList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        student_reference = database.getReference("Students");
        ArrayList<String> enrolled_students = new ArrayList<>();
        Map<Course, ArrayList<String>> my_students = new HashMap<>();

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

        ValueEventListener init_student_list = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    enrolled_students.clear();
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        enrolled_students.add(ds.child("username").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener init_student_map = new ValueEventListener() {
            /**Initialises map of students
             * @author tannergiddings
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    my_students.clear();
                    String username;
                    Course current;

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

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        current = ds.getValue(Course.class);
                        if (current.getInstructor().equals(username)) {
                            student_reference.child(String.valueOf(current.getIndex())).addValueEventListener(init_student_list);
                            my_students.put(current, enrolled_students);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener display_students = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.addValueEventListener(init_student_map);
                String str = "";
                ArrayList<String> current_students;
                for (Course current : my_students.keySet()) {
                    current_students = my_students.get(current);
                    str += current.basicToString() + "\n";
                    for (int i = 0; i < current_students.size(); i++) {
                        str += current_students.get(i) + ", ";
                    }
                    str += "\n";
                }
                view_students.setText(str);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        reference.addValueEventListener(display_students);

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
                    Course current = new Course(name, code);
                    int index = findIndex(current, courseList);

                    if (index == -1) {
                        view_students.setText("Course was not found");
                        return;
                    } else {
                        reference.addValueEventListener(init_student_map);
                        ArrayList<String> current_students = my_students.get(current);
                        String display = "";
                        for (int i = 0; i < current_students.size(); i++) {
                            display = display + current_students.get(i);
                        }
                        view_students.setText(display);
                    }
                }
            }
        });

        view_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(display_students);
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
                course = courseList.get(i);
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    /**
     * Finds a list of courses based on a list of course id
     * @param courseList list of all courses
     * @param integer_list list of indices for which we want the courses
     * @return returns the courses who's indices are in integer_list
     */
    public ArrayList<Course> findCoursesByIds(ArrayList<Integer> integer_list, ArrayList<Course> courseList) {
        ArrayList<Course> new_course_list = new ArrayList<>();
        for (int i = 0; i < courseList.size(); i++) {
            if (integer_list.contains(courseList.get(i).getIndex())) {
                new_course_list.add(courseList.get(i));
            }
        }
        return new_course_list;
    }

    /**
     * Puts the value of the hashmap in a String
     * @author tannergiddings
     * @param my_students list of students registered in a course offered by current instructor
     * @return String value of the elements in the string
     */
    public String toStringMap(HashMap<Course, ArrayList<String>> my_students) {
        String str = "";
        ArrayList<String> current_students;
        for (Course current : my_students.keySet()) {
            str += current.basicToString() + "\n";
            current_students = my_students.get(current);
            for (int i = 0; i < current_students.size(); i++) {
                str += current_students + "\n";
            }
        }
        return str;
    }
}