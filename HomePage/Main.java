package HomePage;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

import Accounts.AdministratorAccount;
import Accounts.FacultyAccount;
import Accounts.StudentAccount;


public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("!!!WELCOME TO COURSE MANAGEMENT SYSTEM!!!");
		System.out.println("Select Module :\n1. ADMINISTRATOR\n2. FACULTY\n3. STUDENT");
		int mod = sc.nextInt();
		switch(mod)
		{
		case 1 :
			System.out.println("Log in Here");
			System.out.println("Enter Username");
			String uname = sc.next();
			System.out.println("Enter Password");
			String passwd = sc.next();
			AdministratorAccount a = new AdministratorAccount();
			a.setUserName(uname);
			a.setPassword(passwd);
			a.validateAdministrator();
			break;
		
		case 2 :
			System.out.println("Log in Here");
			System.out.println("Enter Username");
			String uname2 = sc.next();
			System.out.println("Enter Password");
			String passwd2 = sc.next();
			FacultyAccount f = new FacultyAccount();
			f.setLogInInfo(uname2, passwd2);
			f.isInDatabase();
			break;
		
		case 3 : 
			System.out.println("Log in Here");
		    System.out.println("Enter Username");
		    String uname1 = sc.next();
		    System.out.println("Enter Password");
		    String passwd1 = sc.next();
		    StudentAccount s = new StudentAccount(uname1,passwd1);
		    s.isInDatabase();
		    break;
		
		default : System.out.println("Choose correct module");
		
		
		}       

	}

}
