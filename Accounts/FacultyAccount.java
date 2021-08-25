package Accounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DB.Database;


public class FacultyAccount extends Account{
	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private int hwNumber;
    private String id ;
    public FacultyAccount() {
    }

    public void setLogInInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Check if instructor is in database
     * @return True if instructor is in database
     */
    @Override
    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from facultyAccounts where username = ? and password = ?";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, this.userName);
            pstmt.setString(2, this.password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                initializeAccount(rs);
                ret = true;
            }
            else
            	System.out.println("Please enter valid username and password");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Initialize instructor fields
     * @param rs Result set from query
     */
    private void initializeAccount(ResultSet rs) {

        try {
            this.id = rs.getString(1);
                this.lastName = rs.getString(2);
                this.firstName = rs.getString(3);
            System.out.println("Faculty logged in successfully");
            System.out.println("Welcome "+(firstName+" "+lastName));
          
            try {
				login();
			} catch (IOException e) {
				e.printStackTrace();
			}

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Assign homework from textbox to specified course
     * @param course Course to assign homework to
     * @param homeworkAssignment Homework assignment
     * @throws SQLException 
     * @throws IOException 
     */
    public void assignHw() throws SQLException, IOException {
    	String course = "";
    	System.out.println("courses currently teaching");
    	System.out.println("Enter course id");
    	id = br.readLine();
		String sql = "select coursename from courses where id = ?";
	    PreparedStatement ps = Database.getConnection().prepareStatement(sql);
	    ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		course = rs.getString("coursename");
		
		System.out.println("Please enter your text");
		String text = br.readLine();
		String sql1 = "insert into assignment(id,coursename,description) values(assi_seq.nextval,?,?)";
		PreparedStatement ps1 = Database.getConnection().prepareStatement(sql1);
		ps1.setString(1,course);
		ps1.setString(2,text);
		ps1.executeUpdate();
		System.out.println("work is assigned");
		
        hwNumber = getHwNumberFromDb(course);
        setHwNumberInDB(course, ++hwNumber);
        appendHwToCourseTable(course);
    }

    /**
     * Append to course database homework assignment column
     * @param course Course to add homework to 
     */
    private void appendHwToCourseTable(String course) {
        String sql = "alter table " + course + "course add Hw" + hwNumber + " int";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get total amount of homework for course
     * @param course Course to find total amount of homework
     * @return Total amount of homework
     */
    public int getHwNumberFromDb(String course) {
        int ret = 0;
        String sql = "select numHw from courses where coursename = \'" + course + "\'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("numHw");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Set total amount of homework for course
     * @param course Course to set amount of home for
     * @param numberOfHw Amount of homework assigned
     */
    private void setHwNumberInDB(String course, int numberOfHw) {
        String sql = "update courses set numHw = " + Integer.toString(numberOfHw) + " where coursename = \'" + course + "\'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Set student grade for specified homework assignment
     * @param student Student to look for
     * @param course Course teaching
     * @param hw Homework number
     * @param grade Grade for homework
     */
    public void setGradeInDB(String student, String course, int hw, int grade) {
    	String sql = "update " + course + "course set " + "hw"+ hw + " = \'" + grade + "\' where studentid = \'" + student + "\'";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
            System.out.println("Grade updated successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void login() throws SQLException, IOException
	{
    	int hw = 0;
    	String course = "";
		System.out.println("1. Assign Assignment\n2. Assign Grade");
		String a = br.readLine();
		int assign = Integer.parseInt(a);
		switch(assign)
		{
		case 1 :
			assignHw();
			break;
		case 2 : 
			System.out.println("Pick Student to Grade");
			System.out.println("Students currently instructing");
			String s_id = br.readLine();
		    String student = s_id;
		    System.out.println("Pick HW to Grade");
		    System.out.println("Student = "+student);
		    
		    System.out.println("Enter course id");
	    	id = br.readLine();
		    String query1 = "select coursename from courses where id = ?";
		    pstmt = Database.getConnection().prepareStatement(query1);
		    pstmt.setString(1, id);
		    ResultSet r1 = pstmt.executeQuery();
		    while(r1.next())
		    {
		    course = r1.getString("coursename");
		    }
		    System.out.println("Course Name is : "+course);
		    
		    String query2 = "select numhw from courses where id = ?";
		    pstmt = Database.getConnection().prepareStatement(query2);
		    pstmt.setString(1, id);
		    ResultSet r2 = pstmt.executeQuery();
		    while(r2.next())
		    {
		    hw = r2.getInt("numhw");
		    }
		    
		    System.out.println("Enter marks");
		    String mark = br.readLine();
		    int grade = Integer.parseInt(mark);
			setGradeInDB(student,course,hw,grade);
			break;
		default : System.out.println("Choose Correct Option");
		
		}
	}
}

