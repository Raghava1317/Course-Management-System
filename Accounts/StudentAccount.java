package Accounts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import DB.Database;


public class StudentAccount extends Account {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private ArrayList<String> courses;

    public StudentAccount() {
    }

    public StudentAccount(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    /**
     * Get courses that student is taking
     * @return All courses student is taking
     */
    public ArrayList<String> getCourses() {
        String sql = "select coursename from courses";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            courses = new ArrayList<String>();
            System.out.println("Course is :");
            while(rs.next()) {
                    courses.add(rs.getString(1));
                }
                
               for(int i=0;i<courses.size();i++)
               {
               System.out.println(courses.get(i));
               }
                
            }
       catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    /**
     * Checks if student is in database
     * @return True if student is in database
     */
    @Override
    public boolean isInDatabase() {
        boolean ret = false;

        String sql = "select id, lastname, firstname from StudentAccounts where username = ? and password = ?";
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
     * Initialize all fields for student account
     * @param rs Result set from query
     */
    private void initializeAccount(ResultSet rs) {

        try {
            this.id = rs.getString(1);
            this.lastName = rs.getString(2);
            this.firstName = rs.getString(3);
            System.out.println("Student logged in successfully");
            System.out.println("Welcome "+(firstName+" "+lastName));
            login();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void uploadWork() throws SQLException, FileNotFoundException
    {
    System.out.println("Enter coursename");
    String cc = "";
    try {
    cc = br.readLine();
    } catch (IOException e1) {
    e1.printStackTrace();
    }
    String sql = "select numhw from courses where coursename = ?";
    pstmt = Database.getConnection().prepareStatement(sql);
    pstmt.setString(1, cc);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    int homework = rs.getInt("numhw");
    System.out.println("Homework "+(homework));

     if(homework != 0)
    {
    System.out.println("Homework Description");
    String sql1 = "select description from assignment inner join courses on courses.coursename = assignment.coursename";
    pstmt = Database.getConnection().prepareStatement(sql1);
    ResultSet rs1 = pstmt.executeQuery();
    rs1.next();
    System.out.println(rs1.getString("description"));

     System.out.println("Select file to Upload");
    String filePath = "";
    try {
    filePath = br.readLine();
    } catch (IOException e) {
    e.printStackTrace();
    }

     try {
    String path = "C:\\Users\\Manju\\Desktop\\FINAL PROJECT\\" + filePath;
    String sql2 = "INSERT INTO assignment (workfile) values (?)";
    pstmt = Database.getConnection().prepareStatement(sql2);
    InputStream inputStream = new FileInputStream(new File(path));
    pstmt.setBinaryStream(1, inputStream,(int)path.length());

     int row = pstmt.executeUpdate();
    if (row > 0) {
    System.out.println("File uploaded successfully");
    }
    } catch (SQLException ex) {
    ex.printStackTrace();
    } catch (IOException ex) {
    ex.printStackTrace();
    }
    }
    else
    {
    System.out.println("HW Description");
    System.out.println("No hw assigned");
    System.out.println("Upload hw");
    System.out.println("No hw assigned");
    }
    }
	
	public void checkingGrade() throws ClassNotFoundException, SQLException
	{
		int hw =0;
		String c_id = "",course = "";
		System.out.println("Enter course id");
    	try {
			c_id = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		    String query2 = "select numhw from courses where id = ?";
		    pstmt = Database.getConnection().prepareStatement(query2);
		    pstmt.setString(1, c_id);
		    ResultSet r2 = pstmt.executeQuery();
		    while(r2.next())
		    {
		    hw = r2.getInt("numhw");
		    }
		    //System.out.println(hw);
		    System.out.println("Enter course name");
		    
			try {
				course = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    String query3 ="select  * from " + course + "course where studentid = "+id;
		    pstmt = Database.getConnection().prepareStatement(query3);
		    ResultSet r1 = pstmt.executeQuery();
		    r1.next();
		    System.out.println("Homework grade is "+r1.getInt("hw"+hw));
		    
	}
	
	public void login() 
	{
		
		System.out.println("1. Check courses\n2. Upload HW\n3. Check Grade");
		String o = null;
		try {
			o = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int opt = Integer.parseInt(o);
		
		switch(opt)
		{
		case 1: getCourses();
		        break;
		case 2: try {
				uploadWork();
			} catch (FileNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		       break;
		case 3: try {
				checkingGrade();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		break;
		default : System.out.println("Choose Correct Option");
		
		        
		}
		 
		        
		
	}
	
}
