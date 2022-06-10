package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

class User {
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

/*public class Instructor_Database {
    class I_ListNode {
        class I_Node {
            private Instructor instructor;
            private I_Node next;
            private I_Node prev;

            public I_Node(Instructor instructor) {
                this.instructor = instructor;
            }
        }
        //Instance variables
        private I_Node head;
        private I_Node tail;

        public I_ListNode(){}

        public void add(Instructor newinstructor) {
            I_Node newNode = new I_Node(newinstructor);
            if (head.next == null) {
                head.next = newNode;
                head.prev = tail;
                newNode.prev = head;
                newNode.next = tail;
                tail.next = head;
                tail.prev = newNode;
            } else {
                I_Node swap = head.next;
                head.next = newNode;
                newNode.prev = head;
                swap.prev = newNode;
                newNode.next = swap;
            }
        }

        public boolean contain(Instructor ins) {
            boolean b = false;
            I_Node newNode = head.next;
            while (newNode != tail) {
                b = newNode.instructor.compare(ins);
                if (b == true) {
                    break;
                }
                newNode = newNode.next;
            }
            return b;
        }

        public boolean remove(Instructor ins) {
            boolean b = false;
            I_Node newNode = head.next;
            while (newNode != tail) {
                b = newNode.instructor.compare(ins);
                if (b == true) {
                    I_Node previous = newNode.prev;
                    I_Node following = newNode.next;
                    previous.next = following;
                    following.prev = previous;
                    newNode = following;
                    while (newNode != tail) {
                        newNode = newNode.next;
                    }
                    tail = newNode;
                    head = newNode.next;
                    break;
                }
                newNode = newNode.next;
            }
            return b;
        }
    }
    protected I_ListNode instructor_database = new I_ListNode();

    //Constructor
    public Instructor_Database(){};

    //Method to add an instructor to the list
    public boolean add(String username, String password) {
        Instructor new_instructor = new Instructor(username, password);
        boolean b = instructor_database.contains(new_instructor);
        if (b == false) {
            instructor_database.add(new_instructor);
        }
        return b;
    }

    //Method to remove a course from the database
    public boolean remove(Instructor instructor) {
        return course_database.remove(instructor);
    }
}*/

class Student extends User{
    public Student(String username, String password) {
        super(username, password);
    }
}

/*class Student_Database {
    class S_ListNode {
        class S_Node {
            private Student student;
            private S_Node next;
            private S_Node prev;

            public S_Node(Student student) {
                this.student = student;
            }
        }
        //Instance variables
        private S_Node head;
        private S_Node tail;

        public S_ListNode(){}

        public void add(Student newstudent) {
            S_Node newNode = new S_Node(newstudent);
            if (head.next == null) {
                head.next = newNode;
                head.prev = tail;
                newNode.prev = head;
                newNode.next = tail;
                tail.next = head;
                tail.prev = newNode;
            } else {
                S_Node swap = head.next;
                head.next = newNode;
                newNode.prev = head;
                swap.prev = newNode;
                newNode.next = swap;
            }
        }

        public boolean contain(Student stud) {
            boolean b = false;
            S_Node newNode = head.next;
            while (newNode != tail) {
                b = newNode.student.compare(stud);
                if (b == true) {
                    break;
                }
                newNode = newNode.next;
            }
            return b;
        }

        public boolean remove(Student stud) {
            boolean b = false;
            S_Node newNode = head.next;
            while (newNode != tail) {
                b = newNode.student.compare(stud);
                if (b == true) {
                    S_Node previous = newNode.prev;
                    S_Node following = newNode.next;
                    previous.next = following;
                    following.prev = previous;
                    newNode = following;
                    while (newNode != tail) {
                        newNode = newNode.next;
                    }
                    tail = newNode;
                    head = newNode.next;
                    break;
                }
                newNode = newNode.next;
            }
            return b;
        }
    }
    protected S_ListNode student_database = new S_ListNode();

    //Constructor
    public Student_Database(){};

    public boolean add(String username, String password) {
        Student new_student = new Student(username, password);
        boolean b = student_database.contains(new_student);
        if (b == false) {
            student_database.add(new_student);
        }
        return b;
    }
    //hi

    public boolean remove(Student student) {
        return student_database.remove(student);
    }
}*/

class Course {
    //Instance variables
    private String name;
    private String code;
    private String date;
    private Instructor teacher;

    //Constructor
    public Course(String name, String code) {
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

/*class Course_Database {
    class C_ListNode {
        class C_Node {
            private Course course;
            private C_Node next;
            private C_Node prev;

            public C_Node(Course course) {
                this.course = course;
            }
        }
        //Instance variables
        private C_Node head;
        private C_Node tail;

        public C_ListNode(){}

        public void add(Course newcourse) {
            C_Node newNode = new C_Node(newcourse);
            if (head.next == null) {
                head.next = newNode;
                head.prev = tail;
                newNode.prev = head;
                newNode.next = tail;
                tail.next = head;
                tail.prev = newNode;
            } else {
                C_Node swap = head.next;
                head.next = newNode;
                newNode.prev = head;
                swap.prev = newNode;
                newNode.next = swap;
            }
        }

        public boolean contain(Course cou) {
            boolean b = false;
            C_Node newNode = head.next;
            while (newNode != tail) {
                b = newNode.course.compare(cou);
                if (b == true) {
                    break;
                }
                newNode = newNode.next;
            }
            return b;
        }

        public boolean remove(Course cou) {
            boolean b = false;
            C_Node newNode = head.next;
            while (newNode != tail) {
                b = newNode.course.compare(cou);
                if (b == true) {
                    C_Node previous = newNode.prev;
                    C_Node following = newNode.next;
                    previous.next = following;
                    following.prev = previous;
                    newNode = following;
                    while (newNode != tail) {
                        newNode = newNode.next;
                    }
                    tail = newNode;
                    head = newNode.next;
                    break;
                }
                newNode = newNode.next;
            }
            return b;
        }
    }
    //Database as a linked list
    protected C_ListNode course_database = new C_ListNode();

    //Constructor
    public Course_Database(){};

    //Method to add a course to the list
    public boolean add(String name, String code) {
        Course new_course = new Course(name, code);
        boolean b = course_database.contains(new_course);
        if (b == false) {
            course_database.add(new_course);
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
}*/

class Admin extends User {
    //Constructor
    public Admin(String username, String password) {
        super(username, password);
    }
}

/*public interface Databases {
    protected static Course_Database coursedata = new CourseDatabase();
    protected static Instructor_Database instructordata = new InstructorDatabase();
    protected static Student_Database studentdata = new Student_Database();
}

class Admin_Interface extends Admin implements Databases {
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
}*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
