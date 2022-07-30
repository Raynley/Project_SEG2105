package com.example.deliverable11;

import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Array;
import java.util.ArrayList;


public class TestMethods {

    @Test
    public void testConvertStringToInt(){
        assertEquals(1, welcome_student.convertStringToInt("1"));
        assertEquals(2, welcome_student.convertStringToInt("2"));
        assertEquals(3, welcome_student.convertStringToInt("3"));

    }

    @Test
    public void testCreateIndexStudents(){
        Student first = new Student("first", "pass");
        first.setIndex(0);
        Student second = new Student("second", "pass");
        second.setIndex(1);
        Student third = new Student("third", "pass");
        third.setIndex(2);

        ArrayList<Student> list_1 = new ArrayList<Student>();

        list_1.add(first);
        list_1.add(second);
        list_1.add(third);

        assertEquals(3, welcome_student.createIndexStudents(list_1));

    }

    @Test
    public void testFindCoursesByIds(){
        Course first = new Course("Intro to Software","SEG2105") ;
        first.setIndex(0);
        Course second = new Course("Macroeconomy","SEG2105");
        second.setIndex(1);
        Course third  = new Course("Probability and Statistics", "MAT2377");
        third.setIndex(2);

        ArrayList<Course> list_1 = new ArrayList<Course>();
        list_1.add(first);
        list_1.add(second);
        list_1.add(third);

        ArrayList<Course> list_2 = new ArrayList<Course>();
        list_2.add(first);
        list_2.add(second);

        ArrayList<Integer> list_3 = new ArrayList<Integer>();
        list_3.add(0);
        list_3.add(1);

        assertEquals(list_2, welcome_student.findCoursesByIds(list_3, list_1));
    }

    @Test
    public void testIsOut(){
        int s1 = 1;
        int e1 = 2;
        int s2 = 2;
        int e2 = 4;
        assertTrue(welcome_student.isOut(s1,e1,s2,e2));
        assertFalse(welcome_student.isOut(s2,e1,s1,e2));
    }

    @Test
    public void testFindCourseIndex(){
        Course first = new Course("Intro to Software","SEG2105") ;
        first.setIndex(0);
        Course second = new Course("Macroeconomy","SEG2105");
        second.setIndex(1);
        Course third  = new Course("Probability and Statistics", "MAT2377");
        third.setIndex(2);

        ArrayList<Course> list_1 = new ArrayList<Course>();
        list_1.add(first);
        list_1.add(second);
        list_1.add(third);

        assertEquals(0, welcome_student.findCourseIndex(list_1, first));
    }

}
