package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**Class for methods and functionnality of edit_course.xml. Allows admin to edit course names and codes
 * @author tannergiddings
 */
public class edit_courses extends AppCompatActivity {
    EditText old_code_entry, old_name_entry, new_code_entry, new_name_entry;
    ImageButton edit_btn, del_btn;
    TextView display;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_courses);
        this.setTitle("Edit courses");

        old_code_entry = findViewById(R.id.course_code_to_edit);
        old_name_entry = findViewById(R.id.course_name_to_edit);
        new_code_entry = findViewById(R.id.new_course_code);
        new_name_entry = findViewById(R.id.new_course_name);
        edit_btn = findViewById(R.id.edit_course_btn);
        del_btn = findViewById(R.id.delete_course_btn);
        display = findViewById(R.id.displayCourse);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        ArrayList<Course> courseList = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            /**Displays the courses to the admin*/
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Course.class).toString() + "\n";
                    }
                    display.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        ValueEventListener initList = new ValueEventListener() {
            /**Initializes 'courseList' with all the courses*/
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

        reference.addValueEventListener(postListener);
        reference.addValueEventListener(initList);

        edit_btn.setOnClickListener(new View.OnClickListener(){
            /**Verifies if there is a valid course entered then does the changes required
             * @author tannergiddings
             */
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(initList);
                String old_code = old_code_entry.getText().toString().trim();
                String old_name = old_name_entry.getText().toString().trim();
                String new_code = new_code_entry.getText().toString().trim();
                String new_name = new_name_entry.getText().toString().trim();

                if (TextUtils.isEmpty(old_code) && TextUtils.isEmpty(old_name) && TextUtils.isEmpty(new_code) && TextUtils.isEmpty(new_name)) {
                    old_code_entry.setError("Course code required");
                    old_name_entry.setError("Course name required");
                    new_code_entry.setError("New course details required");
                    new_name_entry.setError("New course details required");
                    return;
                } else if (TextUtils.isEmpty(old_code) && TextUtils.isEmpty(old_name)) {
                    old_code_entry.setError("Course code required");
                    old_name_entry.setError("Course name required");
                    return;
                } else if (TextUtils.isEmpty(old_code)) {
                    old_code_entry.setError("Course code required");
                    return;
                } else if (TextUtils.isEmpty(old_name)) {
                    old_name_entry.setError("Course name required");
                    return;
                } else if (TextUtils.isEmpty(new_code) && TextUtils.isEmpty(new_name)) {
                    new_code_entry.setError("New course details required");
                    new_name_entry.setError("New course details required");
                    return;
                } else if (TextUtils.isEmpty(new_code)) {
                    new_code = old_code;
                    int index = getIndex(new Course(new_name, new_code), courseList);
                    if (index < 0) {
                        old_code_entry.setText("Course not found");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                        return;
                    } else {
                        reference.child(String.valueOf(index)).child("name").setValue(new_name);
                        old_code_entry.setText("");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                    }
                } else if (TextUtils.isEmpty(new_name)) {
                    new_name = old_name;
                    int index = getIndex(new Course(new_name, new_code), courseList);
                    if (index < 0) {
                        old_code_entry.setText("Course not found");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                        return;
                    } else {
                        reference.child(String.valueOf(index)).child("code").setValue(new_code);
                        old_code_entry.setText("");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                    }
                } else {
                    int index = getIndex(new Course(old_name, old_code), courseList);
                    if (index < 0) {
                        old_code_entry.setText("Course not found");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                        return;
                    } else {
                        reference.child(String.valueOf(index)).child("name").setValue(new_name);
                        reference.child(String.valueOf(index)).child("code").setValue(new_code);
                        old_code_entry.setText("");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                    }
                }
                reference.addValueEventListener(postListener);
            }
        });
        
        del_btn.setOnClickListener(new View.OnClickListener(){
            /**Verifies if there is such a course and deletes it from the database*/
            @Override
            public void onClick(View v) {
                String old_code = old_code_entry.getText().toString().trim();
                String old_name = old_name_entry.getText().toString().trim();
                
                if (TextUtils.isEmpty(old_code) && TextUtils.isEmpty(old_name)) {
                    old_code_entry.setError("Course code required");
                    old_name_entry.setError("Course name required");
                    return;
                } else if (TextUtils.isEmpty(old_code)) {
                    old_code_entry.setError("Course code required");
                    return;
                } else if (TextUtils.isEmpty(old_name)) {
                    old_name_entry.setError("Course name required");
                    return;
                } else {
                    //REMOVE FROM DATABASE
                    int index = getIndex(new Course(old_name, old_code), courseList);
                    if (index < 0) {
                        old_code_entry.setText("Course not found");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                        return;
                    } else {
                        reference.child(String.valueOf(index)).removeValue();
                        old_code_entry.setText("");
                        old_name_entry.setText("");
                        new_code_entry.setText("");
                        new_name_entry.setText("");
                    }
                }
                reference.addValueEventListener(postListener);
            }
        });
    }

    /**find the index for a specific course in the database
     * @author tannergiddings
     * @param course course's index to be found
     * @param courseList arrayList in which the course is to be found
     * @return index of the course or -1 if course not found
     */
    public int getIndex(Course course, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }
}
