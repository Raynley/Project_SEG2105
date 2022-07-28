package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**Methods and functionnalities for welcome_student.xml
 * @author tannergiddings
 */
public class welcome_student extends AppCompatActivity {
    EditText name_entry, code_entry, day_entry;
    ImageButton add_btn, remove_btn, search_btn;
    TextView displayCourses, error_display, course_view;
    ArrayList<Course> courseList;
    FirebaseDatabase database;
    DatabaseReference reference, student_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_student);

        name_entry = findViewById(R.id.course_name_to_edit);
        code_entry = findViewById(R.id.course_code_to_edit);
        day_entry = findViewById(R.id.daySearch);
        add_btn = findViewById(R.id.add_btn);
        remove_btn = findViewById(R.id.delete_course_btn);
        search_btn = findViewById(R.id.search_btn);
        displayCourses = findViewById(R.id.displayCourse);
        error_display = findViewById(R.id.Error);
        course_view = findViewById(R.id.Enrol_View);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Courses");
        student_reference = database.getReference("Students");
        courseList = new ArrayList<>();
        ArrayList<Course> enrolled_courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

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

        ValueEventListener postListener = new ValueEventListener() {
            /**Displays the course to the user
             * @author tannergiddings
             * @param snapshot database snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Course.class).stud_toString() + "\n";
                    }
                    displayCourses.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        ValueEventListener init_students = new ValueEventListener() {
            /**Initialises list of students, a.k.a students
             * @author tannergiddings
             * @param snapshot database snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        students.add(ds.getValue(Student.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener init_enrolled_courses = new ValueEventListener() {
            /**Gets the courses in which the user is enrolled in
             * @author tannergiddings
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course current;
                    String textDisplay = "";
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
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        reference.addValueEventListener(initList);
                        current = ds.getValue(Course.class);
                        int index;
                        for (DataSnapshot ds_student : ds.child("Students").getChildren()) {
                            if (ds_student.child("username").equals(username)) {
                                index = ds_student.getValue(String);
                            }
                            /*
                            if (ds_student.getValue(Student.class).getUsername().equals(username)) {
                                enrolled_courses.add(current);
                                break;
                            }
                            */
                             */
                        }
                    }
                    for (int i = 0; i < enrolled_courses.size(); i++) {
                        textDisplay = textDisplay + enrolled_courses.get(i).basicToString();
                    }
                    course_view.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener initList = new ValueEventListener() {
            /**Initialises the list of courses
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

        reference.addValueEventListener(postListener);
        reference.addValueEventListener(init_enrolled_courses);
        /*
        Having trouble adding student to database.
        Have to verify if you can add ArrayLists to databases.
        Possible changes required.
         */
        add_btn.setOnClickListener(new View.OnClickListener() {
            /**Adds the student to the course
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                String name = name_entry.getText().toString();
                String code = code_entry.getText().toString();
                reference.addValueEventListener(initList);
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
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(code)) {
                    name_entry.setText("Name required");
                    code_entry.setText("Code required");
                    error_display.setText("");
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    name_entry.setText("Name required");
                    error_display.setText("");
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    code_entry.setText("Code required");
                    error_display.setText("");
                    return;
                }
                reference.addValueEventListener(initList);
                int index = getIndex(new Course(name, code), courseList);
                if (index < 0) {
                    error_display.setText("Course not found");
                    return;
                } else {
                    reference.child(String.valueOf(index)).addValueEventListener(init_students);
                    boolean inCourse = inCourse(username, students);
                    if (inCourse) {
                        error_display.setText("You are already enrolled in this course");
                        return;
                    } else {
                        Course current = findCourse(index, courseList);
                        if (current.addStudent()) {
                            reference.child(String.valueOf(index)).child("number_of_students").setValue(current.getNumber_of_students());
                            Student current_student = new Student();
                            current_student.setUsername(username);
                            current_student.setIndex(createIndexStudents(students));
                            student_reference.child(String.valueOf(index)).child(String.valueOf(current_student.getIndex())).setValue(current_student);
                            error_display.setText("");
                            name_entry.setText("");
                            code_entry.setText("");
                        } else {
                            error_display.setText("Course at capacity. Enrolment failed");
                        }
                    }
                }
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            /**Allows user to search for a course based on days offered, code or name
             * @author tannergiddings
             * @param v
             */

            @Override
            public void onClick(View v) {
                reference.addValueEventListener(initList);
                String name = name_entry.getText().toString().trim();
                String code = code_entry.getText().toString().trim();
                String days = day_entry.getText().toString().trim();
                ArrayList<Course> courses_to_display = courseList;

                if (!TextUtils.isEmpty(name)) {
                    courses_to_display = compareName(courses_to_display, name);
                }

                if (!TextUtils.isEmpty(code)) {
                    courses_to_display = compareCode(courses_to_display, code);
                }

                if (!TextUtils.isEmpty(days)) {
                    courses_to_display = findDaysAmongCourses(courses_to_display, days);
                }

                displayCourses.setText(toStringList(courses_to_display));
            }
        });
        displayCourses.setOnClickListener(new View.OnClickListener() {
            /**displays courses
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(postListener);
            }
        });
        reference.addValueEventListener(postListener);
    }

    /**finds index for a course
     * @author tannergiddings
     * @param course course who's index is to be found
     * @param courseList where it can be found
     * @return the index, if not found, returns -1.
     */
    public int getIndex(Course course, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    /**Verifies if user is registered in a course
     * @author tannergiddings
     * @param username
     * @param studentList
     * @return
     */
    public boolean inCourse(String username, ArrayList<Student> studentList) {
        for (int i = 0; i < studentList.size(); i++) {
            if (username.equals(studentList.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**Finds course in list based on index
     * @author tannergiddings
     * @param index index of course
     * @param courseList list of courses
     * @return course if found. If not, returns null
     */
    public Course findCourse(int index, ArrayList<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getIndex() == index) {
                return courseList.get(i);
            }
        }
        return null;
    }

    /**Enters a list in a new list
     * @author tannergiddings
     * @param list1 list to be entered in list 2
     * @param list2 list in which list1 is entered
     */
    private void enterDays(String[] list1, String[] list2) {
        for (int i = 0; i < list1.length; i++) {
            list2[i] = list1[i];
        }
    }

    /**finds the days in which the course is offered
     * @author tannergiddings
     * @param course course who's days are to be returned
     * @return the days in which this course is offered
     */
    private String[] findDays(Course course) {
        String[] splitCourses = course.getDays().split(",");
        int length = 1;
        String[] listDays = new String[0];
        String[] swap;
        for (int i = 0; i < splitCourses.length; i++) {
            String currentCourse = splitCourses[i].trim();
            if (currentCourse.equals("Monday") || currentCourse.equals("monday") || currentCourse.equals("Mondays") || currentCourse.equals("mondays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "monday";
                listDays = swap;
                length++;
            } else if (currentCourse.equals("Tuesday") || currentCourse.equals("tuesday") || currentCourse.equals("Tuesdays") || currentCourse.equals("tuesdays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "tuesday";
                listDays = swap;
                length++;
            } else if (currentCourse.equals("Wednesday") || currentCourse.equals("wednesday") || currentCourse.equals("Wednesdays") || currentCourse.equals("wednesdays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "wednesday";
                listDays = swap;
                length++;
            } else if (currentCourse.equals("Thurday") || currentCourse.equals("thursday") || currentCourse.equals("Thursdays") || currentCourse.equals("thursdays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "thursday";
                listDays = swap;
                length++;
            } else if (currentCourse.equals("Friday") || currentCourse.equals("friday") || currentCourse.equals("Fridays") || currentCourse.equals("fridays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "friday";
                listDays = swap;
                length++;
            } else if (currentCourse.equals("Saturday") || currentCourse.equals("saturday") || currentCourse.equals("Saturdays") || currentCourse.equals("saturdays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "saturday";
                listDays = swap;
                length++;
            } else if (currentCourse.equals("Sunday") || currentCourse.equals("sunday") || currentCourse.equals("Sundays") || currentCourse.equals("sundays")) {
                swap = new String[length + 1];
                enterDays(listDays, swap);
                swap[length] = "saturday";
                listDays = swap;
                length++;
            }
        }
        return listDays;
    }

    public boolean findDayAmongString(String string_of_days, String day) {
        String[] days = string_of_days.split(",");
        for (int i = 0; i < days.length; i++) {
            if (day.equals(days[i].trim())) {
                return true;
            }
        }
        return false;
    }

    /**Finds the courses which are offered on a specific day
     * @author tannergiddings
     * @param courses list of courses to be verified
     * @param day day of the week to be used to compare courses
     * @return arraylist of courses offered on a day
     */
    public ArrayList<Course> findDaysAmongCourses(ArrayList<Course> courses, String day) {
        ArrayList<Course> courseList = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            if (findDayAmongString(courseList.get(i).getDays(), day.trim())) {
                courseList.add(courses.get(i));
            }
        }
        return courseList;
    }

    /**Finds a list of courses with the entered code
     * @author tannergiddings
     * @param list arraylist of courses
     * @param code code to compare courses with
     * @return list with courses with the code
     */
    public ArrayList<Course> compareCode(ArrayList<Course> list, String code) {
        ArrayList<Course> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals(code)) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    /**Finds a list of courses with the entered name
     * @author tannergiddings
     * @param list arraylist of courses
     * @param name name to compare courses with
     * @return list with courses with the name
     */
    public ArrayList<Course> compareName(ArrayList<Course> list, String name) {
        ArrayList<Course> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    /**Returns a string of the courses in the arrayList
     * @author tannergiddings
     * @param list arraylist to turn into a string
     * @return a string of the courses
     */
    public String toStringList(ArrayList<Course> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i).toString();
        }
        return str;
    }

    /**Returns a valid index for studentList
     * @author tannergiddings
     * @param student_list arraylist of studnets
     * @return an index or -1 if error
     */
    public int createIndexStudents(ArrayList<Student> student_list) {
        if (student_list.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (int i = 0; i < student_list.size(); i++) {
                sum += student_list.get(i).getIndex();
            }
            return sum;
        }
    }
}