package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<Integer, List<String>> all_enrolled_students;

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
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Integer> enrolled_courses_id = new ArrayList<>();
        Map<String, Integer> student_keys_list = new HashMap<>();

        ValueEventListener init_student_keys = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    student_keys_list.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        student_keys_list.put(ds.child("username").getValue(String.class), ds.child("index").getValue(Integer.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

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
                    reference.addValueEventListener(initList);
                    String username;
                    ArrayList<Integer> key_list = new ArrayList<>();
                    ArrayList<Course> course_list;

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
                        for (DataSnapshot ds_sub : ds.getChildren()) {
                            if (ds_sub.child("username").getValue().equals(username)) {
                                key_list.add(Integer.parseInt(ds.getKey()));
                                break;
                            }
                        }
                    }

                    course_list = findCoursesByIds(key_list, courseList);
                    String text = "";

                    for (int i = 0; i < course_list.size(); i++) {
                        text = text + course_list.get(i).basicToString() + "\n";
                    }

                    course_view.setText(text);

                    /*
                    Course current;
                    String textDisplay = "";
                    String username;
                    String current_student;
                    all_enrolled_students = new HashMap<>();

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
                        enrolled_courses_id.clear();
                        int index;
                        for (DataSnapshot ds_student : ds.child("Students").getChildren()) {
                            current_student = ds_student.child("username").getValue(String.class);
                            if (current_student.equals(username)) {
                                index = Integer.parseInt(ds_student.getKey());
                                current = findCourse(index, courseList);
                                if (current != null) {
                                    textDisplay = textDisplay + current.stud_toString() + "\n";
                                    enrolled_courses_id.add(index);
                                }
                            }
                        }
                    }
                    course_view.setText(textDisplay);
                    */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        student_reference.addValueEventListener(init_enrolled_courses);
        reference.addValueEventListener(postListener);
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
                        student_reference.child(String.valueOf(current.getIndex())).addValueEventListener(init_student_keys);
                        if (!student_keys_list.containsKey(username)) {
                            if (current.addStudent()) {
                                reference.child(String.valueOf(index)).child("number_of_students").setValue(current.getNumber_of_students());
                                Student current_student = new Student();
                                current_student.setUsername(username);
                                current_student.setIndex(createIndexStudents(students));
                                student_reference.child(String.valueOf(index)).child(String.valueOf(current_student.getIndex())).setValue(current_student);
                                error_display.setText("");
                                name_entry.setText("");
                                code_entry.setText("");
                                reference.addValueEventListener(init_enrolled_courses);
                                return;
                            } else {
                                error_display.setText("Course at capacity. Enrolment failed");
                                return;
                            }
                        } else {
                            error_display.setText("You are already enrolled in this course");
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

        remove_btn.setOnClickListener(new View.OnClickListener() {
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
                student_reference.addValueEventListener(init_enrolled_courses);
                ArrayList<Course> enrolled_courses = findCoursesByIds(enrolled_courses_id, courseList);
                Course current = findCourseInList(courseList, new Course(name, code));

                if (current == null) {
                    error_display.setText("Course not found");
                    return;
                } else {
                    int index = current.getIndex();
                    student_reference.child(String.valueOf(index)).addValueEventListener(init_student_keys);
                    if (student_keys_list.containsKey(username)) {
                        int current_student_index = student_keys_list.get(username);
                        student_reference.child(String.valueOf(index)).child(String.valueOf(current_student_index)).removeValue();
                        current.remove_number_of_students();
                        reference.child(String.valueOf(index)).child("number_of_students").setValue(current.getNumber_of_students());
                        return;
                    } else {
                        error_display.setText("You are not enrolled in this course");
                        return;
                    }
                }


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

        course_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_reference.addValueEventListener(init_enrolled_courses);
            }
        });
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

    /**Verifies if student is in list of students
     * @author tannergiddings
     * @param student student to look for
     * @param studentList list in which to look for
     * @return returns a boolean value showing if student is in studentList
     */
    public boolean findEnrolmentStudent(ArrayList<String> studentList, String student) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).equals(student)) {
                return true;
            }
        }
        return false;
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
     * Finds a course in a list and returns its index
     * @author tannergiddings
     * @param courseList list of courses
     * @param course course to find
     * @return index of course
     */
    public int findCourseIndex(ArrayList<Course> courseList, Course course) {
        for (int i = 0; i < courseList.size(); i++) {
            if (course.equals(courseList.get(i))) {
                return courseList.get(i).getIndex();
            }
        }
        return -1;
    }

    /**
     * Finds a course in a list
     * @author tannergiddings
     * @param courseList list of courses
     * @param course to find
     * @return complete course info
     */
    public Course findCourseInList(ArrayList<Course> courseList, Course course) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).equals(course)) {
                return courseList.get(i);
            }
        }
        return null;
    }
}