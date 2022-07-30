package com.example.deliverable11;

/**Allows creation of admins using the 'User' superclass
 * @author tannergiddings
 */
public class Admin extends User{

    /** Constructor. Uses User's constructor
     * @author tannergiddings
     * @param username username for new Admin
     * @param password password for new Admin
     */
    public Admin(String username, String password) {
        super(username, password);
    }

    /**Constructor without inputs. Used for database
     * @author tannergiddings
     */
    public Admin(){}
}
