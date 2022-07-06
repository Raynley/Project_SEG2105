package com.example.deliverable11;

import org.junit.Test;
import static org.junit.Assert.*;


public class TestMethods {

    @Test
    public  void coursesAreEqual(){
       Course first = new Course("Intro to Software","SEG2105") ;
       Course second = new Course("Macroeconomy","SEG2105");
       Course s = new Course("Intro to Software","SEG2105");
       Course v = new Course("Intro to Software","SEG 2105");
       assertFalse(first.equals(second));
       assertFalse(first.equals(v));
       assertTrue(first.equals(s));

    }

    @Test
    public void matchPasswordTest(){
        String a = "password";
        String b = "Password";
        assertFalse(SignUp.passwordMatch(a,b));
    }

    @Test
    public void Correct_lengthTest(){
        String a = "pa";
        String b = "Password";
        assertFalse(SignUp.isCorrectLength(a));
        assertTrue(SignUp.isCorrectLength(b));

    }

    @Test
    public void file_not_empty_test(){
        String a = "";
        String c;
        String b = "Password";
        assertTrue(MainActivity.nameEmpty(a));
        assertFalse(MainActivity.nameEmpty(b));
        //assertTrue(MainActivity.nameEmpty(c));

    }


    @Test
    public void correct_capacity_format(){
        String a = "120";
        String c;
        String b = "Password";
        assertTrue(welcome_instructor.isValidCapacity(a));
        assertFalse(welcome_instructor.isValidCapacity(b));

    }



}
