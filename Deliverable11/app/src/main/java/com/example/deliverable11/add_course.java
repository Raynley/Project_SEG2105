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

/** Java code for add_course.xml file. Allows the admin to add a course to the database.
 * @author tannergiddings
 */
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
        rootCourse = FirebaseDatabase.getInstance();
        reference = rootCourse.getReference("Courses");
        ArrayList<Course> course_list = new ArrayList<>();

        ValueEventListener initList = new ValueEventListener() {

            /** Initialises 'ArrayList<Course>' course_list with all the courses
             * @author tannergiddings
             */
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
        reference.addValueEventListener(initList);

        course_btn.setOnClickListener(new View.OnClickListener() {
            /**When the course button is clicked. A new course is created and added to the database
             * @author tannergiddings
             * @param v
             */
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
                    String name = course_name.getEditableText().toString();
                    String code = course_code.getEditableText().toString();
                    int index = createIndexCourses(course_list);
                    if (index >= 0) {
                        Course course = new Course(name, code);
                        course.setIndex(index);
                        reference.child(String.valueOf(index)).setValue(course);
                        course_name.setText("");
                        course_code.setText("");
                    }
                }
            }
        });

    }

    /**Returns a valid index for Courses
     * @author tannergiddings
     * @param course_list arraylist of courses
     * @return an index or -1 if error
     */
    public int createIndexCourses(ArrayList<Course> course_list) {
        boolean found;
        if (course_list.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (int i = 0; i < course_list.size(); i++) {
                sum += course_list.get(i).getIndex();
            }
            return sum;
        }
    }


}
