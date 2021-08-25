package Courses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import DB.Database;

public class Course {
	
	Scanner sc = new Scanner(System.in);
	PreparedStatement pstmt;
    private String name;
    private String id;
    private ArrayList<String> instructorslist;
    private ArrayList<String> studentsList;

    

    /**
     * When course is created, separate course table is created containing who is in the course
     */
    private void createTable() {
        String sql = "create table " + name + "Course ( id varchar2(4), instructorid varchar2(4), studentId varchar2(4) )";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
            
            insertIntoCourseTable();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * When course is created, add to list of total courses
     */
    private void insertIntoCourseTable() {
        String sql = "insert into courses (id, coursename, numHw) values (?, ?, ?)";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, 0);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get all instructors in database
     */
    private void getInstructorsInCourseDB() {

    	System.out.println("Select Faculty");
        String sql = "select id, firstname, lastname from facultyaccounts";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            instructorslist = new ArrayList<String>();
            while (rs.next()) {
                instructorslist.add(rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3));
            }
            System.out.println("Faculty :");
            for(int i=0;i<instructorslist.size();i++)
            {
            	System.out.println(instructorslist.get(i));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get all students in database
     */
    private void getStudentsDB() {

    	System.out.println("Select Students : ");
        String sql = "select id, firstname, lastname from studentaccounts";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            studentsList = new ArrayList<String>();
            while (rs.next()) {
                studentsList.add(rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3));
            }
            System.out.println("Students :");
            for(int i=0;i<studentsList.size();i++)
            {
            	System.out.println(studentsList.get(i));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Course validation method, checks fields to ensure course has met requirements
     * @return Page to go to
     */
    public void courseCreationProcess() {
     
    	 System.out.println("Enter course Name");
         name = sc.next();
         System.out.println("Enter Course ID");
         id = sc.next();
         createTable();
         getInstructorsInCourseDB();
         getStudentsDB();
         insertIntoDB();
         System.out.println("Course created successfully!!!");
       
     }
   
    /**
     * Insert into course database the instructors and students taking course
     */
    private void insertIntoDB() {
        String sql = "insert into " + name + "course (id, instructorid) values (?, ?)";
        try {
            pstmt = Database.getConnection().prepareStatement(sql);
               for(int i = 0;i<instructorslist.size();i++)
               {
                pstmt.setString(1, id);
                String instructorID = instructorslist.get(i);
                String a[] = instructorID.split(" ");
                pstmt.setString(2, a[0]);
                pstmt.executeUpdate();
               }
               
               sql = "insert into " + name + "course (id,studentid) values (?, ?)";
               pstmt = Database.getConnection().prepareStatement(sql);
               for(int i = 0;i<studentsList.size();i++)
               {
            	pstmt.setString(1, id);    
                String studentID = studentsList.get(i);
                String s[] = studentID.split(" ");
                pstmt.setString(2, s[0]);
                pstmt.executeUpdate();
                
               }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

