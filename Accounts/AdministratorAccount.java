package Accounts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import Courses.Course;
import DB.Database;

public class AdministratorAccount {

	private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String id;
    private PreparedStatement pstmt;

    /**
     * Creates a new instance of AdministratorAccount
     */
    public AdministratorAccount() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    /**
     * As long as fields are not blank and information is correct, login
     * administrator
     *
     * @throws ClassNotFoundException 
     */
    public void validateAdministrator() throws ClassNotFoundException {
        
        if (userName.equals("") || password.equals("")) {
            System.out.println("Please enter valid Username and Password");
        } else {
            if (isInDatabase()) {
               
            } else {
                System.out.println("Invalid Username and Password");
            }
        }
       
    }

    /**
     * Check if administrator is in database
     *
     * @return True if administrator is in database
     * @throws ClassNotFoundException 
     */
    public boolean isInDatabase() throws ClassNotFoundException {
        boolean ret = false;

        String sql = "select id, lastname, firstname from AdministratorAccounts where username = ? and password = ?";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.userName);
            pstmt.setString(2, this.password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                initializeAccount(rs);
                ret = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Setup all fields of administrator account
     *
     * @param rs Result set from query
     * @throws ClassNotFoundException 
     */
    private void initializeAccount(ResultSet rs) throws ClassNotFoundException {

        try {
            this.id = rs.getString(1);
            this.lastName = rs.getString(2);
            this.firstName = rs.getString(3);
            System.out.println("Admin logged in successfully");
            System.out.println("Hello "+(firstName+lastName));
            login();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void login() throws ClassNotFoundException, SQLException
	{
    	Scanner sc = new Scanner(System.in);
		System.out.println("1. Create Student Account\n2. Create Faculty Account\n3. Create Courses");
		int acc = sc.nextInt();
		switch(acc)
		{
		case 1: StudentCreationAccount s = new StudentCreationAccount();
		        s.registrationProcess();
		        break;
		case 2 : FacultyCreationAccount f = new FacultyCreationAccount();
		         f.registrationProcess();
		         break;
		case 3 : Course c = new Course();
		         c.courseCreationProcess();
		         break;
		default : System.out.println("Choose Correct Option");
		}
	}
}

