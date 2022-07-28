package com.example.deliverable11;

/**User class. Parent class to Admin, Instructor and Student
 * @author tannergiddings
 */
public class User {
    protected String username;
    protected String password;
    protected int index;

    /**Constructor for user
     * @author tannergiddings
     * @param username new username for user
     * @param password new password for user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**Constructor without parameters*/
    public User(){}

    /**getter for username
     * @author tannergiddings
     * @return username of user
     */
    public String getUsername() {
        return username;
    }

    /**getter for password
     * @author tannergiddings
     * @return password of user
     */
    public String getPassword() {
        return password;
    }

    /**setter for username
     * @author tannergiddings
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**setter for password
     * @author tannergiddings
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**verifies entry with password
     * @author tannergiddings
     * @param entry entry to compare with password
     * @return if they equals each other
     */
    public boolean verify(String entry) {
        return password.equals(entry);
    }

    /**Returns username of user
     * @author tannergiddings
     * @return username
     */
    public String toString() {
        return username;
    }

    /**getter for index
     * @author tannergiddings
     * @return index of user
     */
    public int getIndex() {
        return index;
    }

    /**setter for index
     * @author tannergiddings
     * @param index new index
     */
    public void setIndex(int index) {
        this.index = index;
    }
}

