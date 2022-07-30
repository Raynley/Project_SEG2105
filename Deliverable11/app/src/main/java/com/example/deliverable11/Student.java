package com.example.deliverable11;

/**Student class which extends User class
 * @author tannergiddings
 */
public class Student extends User{

    /**Constructor which uses the constructor for User
     * @author tannergiddings
     * @param username new username for student
     * @param password new password for student
     */
    public Student(String username, String password) {
        super(username, password);
    }

    /**Constructor without parameters
     * @author tannergiddings
     */
    public Student(){}
}
