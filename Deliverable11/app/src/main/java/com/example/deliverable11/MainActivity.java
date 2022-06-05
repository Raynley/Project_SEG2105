package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public class User {
        private String username;
        private String password;
        private boolean access;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public boolean verify_password(String entry) {
            if (password.equals(entry)) {
                access = true;
                return true;
            } else {
                return false;
            }
        }

        public boolean getAccess(){
            return access;
        }

        public boolean compare(User other) {
            return username.equals(other.username);
        }
    }

    public class Instructor extends User {
        public Instructor(String username, String password) {
            super(username, password);
        }
    }

    public class Instructor_Database {
        protected LinkedList<Instructor> instructor_database = new LinkedList<Instructor>();

        //Constructor
        public Instructor_Database(){};

        //Method to add an instructor to the list
        public boolean add(String username, String password) {
            Instructor new_instructor = new Instructor(username, password);
            boolean b = instructor_database.contains(new_instructor);
            if (b == false) {
                instructor_database.addLast(new_instructor);
            }
            return b;
        }

        //Method to remove a course from the database
        public boolean remove(Instructor instructor) {
            return course_database.remove(instructor);
        }
    }

    public class Student extends User{
        public Student(String username, String password) {
            super(username, password);
        }
    }

    public class Student_Database {
        protected LinkedList<Student> student_database = new LinkedList<Student>();

        //Constructor
        public Student_Database(){};

        public boolean add(String username, String password) {
            Student new_student = new Student(username, password);
            boolean b = student_database.contains(new_student);
            if (b == false) {
                student_database.addLast(new_student);
            }
            return b;
        }

        public boolean remove(Student student) {
            return student_database.remove(student);
        }
    }

    public class Course {
        //Instance variables
        private String name;
        private String code;
        private String date;
        private Instructor teacher;

        //Constructor
        public course(String name, String code) {
            this.name = name;
            this.code = code;
        }

        //Method to edit name of course
        public void edit_name(String name) {
            this.name = name;
        }

        //Method to edit code of course
        public void edit_code(String code) {
            this.code = code;
        }

        //Method to edit the date of a course
        public void edit_date(String date) {
            this.date = date;
        }

        public boolean compare(Course newcourse) {
            return name.equals(newcourse.name) && code.equals(newcourse.code);
        }
    }

    public class Course_Database {
        //Database as a linked list
        protected LinkedList<Course> course_database = new LinkedList<Course>();

        //Constructor
        public Course_Database(){};

        //Method to add a course to the list
        public boolean add(String name, String code) {
            Course new_course = new Course(name, code);
            boolean b = course_database.contains(new_course);
            if (b == false) {
                course_database.addLast(new_course);
            }
            return b;
        }

        //Method to remove a course from the database
        public boolean remove(Course course) {
            return course_database.remove(course);
        }

        //Method to edit the name and/or the code of a course
        public boolean edit_course(Course course, String name, String code) {
            int location = course_database.indexOf(course);
            if (location >= 0) {
                Course newcourse = new Course(name, code);
                course_database.add(location, newcourse);
            }
            return location >= 0;
        }

        //Method to edit the date of a course
        public boolean edit_date(Course course, String date) {
            int location = course_database.indexOf(course);
            if (location >= 0) {
                Course newcourse = course.edit_name(date);
                course_database.add(location,newcourse);
            }
            return location >= 0;
        }
    }

    public class Admin extends User {
        //Constructor
        public Admin(String username, String password) {
            super(username, password);
        }

    }

    public interface Databases {
        protected static Course_Database coursedata = new CourseDatabase();
        protected static Instructor_Database instructordata = new InstructorDatabase();
        protected static Student_Database studentdata = new Student_Database();
    }

    public class Admin_Interface extends Admin implements Databases {
        Admin main_admin = new Admin("admin", "admin123");

        public boolean addCourse(String name, String code) {
            if (main_admin.getAccess() == true) {
                return coursedata.add(name, course);
            } else {
                return false;
            }
        }

        public boolean editCourse(String init_name, String init_code, String name, String code) {
            if (main_admin.getAccess() == true) {
                Course initcourse = new Course(init_name, init_code);
                return coursedata.edit_course(initcourse, name, course);
            } else {
                return false;
            }
        }

        public boolean editDate(String name, String code, String date) {
            if (main_admin.getAccess() == true) {
                Course initcourse = new Course(name, code);
                return coursedata.edit_date(initcourse, date);
            } else {
                return false;
            }
        }

        public boolean removeCourse(String name, String code) {
            if (main_admin.getAccess() == true) {
                Course initcourse = new Course(name, code);
                return coursedata.remove(initcourse);
            } else {
                return false;
            }
        }

        public boolean removeInstructor(Instructor instructor) {
            if (main_admin.getAccess() == true) {
                return instructordata.remove(instructor);
            } else {
                return false;
            }
        }

        public boolean removeStudent(Student student) {
            if (main_admin.getAccess() == true) {
                return studentdata.remove(student);
            } else {
                return false;
            }
        }
    }
}
