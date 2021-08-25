package Accounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import DB.Database;

public class StudentCreationAccount {
	
	Scanner sc = new Scanner(System.in);
	
	private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String id;
    private PreparedStatement pstmt;
    private String confirmUserName;
    private String confirmPassword;


    public void registrationProcess() {
        
        System.out.println("Enter username");
    	userName = sc.next();
    	System.out.println("Enter password");
    	password = sc.next();
    	System.out.println("Enter confirm password");
    	confirmPassword = sc.next();
    	System.out.println("Enter confirm username");
    	confirmUserName = sc.next();
    	System.out.println("Enter first name");
    	firstName = sc.next();
    	System.out.println("Enter last name");
    	lastName = sc.next();
    	System.out.println("Enter ID");
    	id = sc.next();
    	
        if ((!userName.equals(confirmUserName)) || (!password.equals(confirmPassword))) 
        {
        	System.out.println("Check confirmUserName and confirmPassword");
        	
        } 
        else 
        {
            if (isInDatabase()) 
            {
            	
            } 
            else 
            {
                insertStudentIntoDB();
            }
        }
      
    }

    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from StudentAccounts where username = ? and password = ?";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.userName);
            pstmt.setString(2, this.password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    private void insertStudentIntoDB() {
        String sql = "insert into StudentAccounts(id, lastname, firstname, username, password) values (?, ?, ?, ?, ?)";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.id);
            pstmt.setString(2, this.lastName);
            pstmt.setString(3, this.firstName);
            pstmt.setString(4, this.userName);
            pstmt.setString(5, this.password);
            pstmt.executeUpdate();
            System.out.println("Student record inserted successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
