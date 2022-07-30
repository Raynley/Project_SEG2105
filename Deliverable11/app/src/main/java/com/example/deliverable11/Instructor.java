package com.example.deliverable11;

/** Class for instructor
 * @author tannergiddings
 */
public class Instructor extends User{

    /**Constructor using the constructor from User
     * @author tannergiddings
     * @param username new username for instructor
     * @param password new password for instructor
     */
    public Instructor(String username, String password) {
        super(username, password);
    }

    /**Constructor without parameters
     * @author tannergiddings
     */
    public Instructor(){}
}
