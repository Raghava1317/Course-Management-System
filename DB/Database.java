package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final static String className = "oracle.jdbc.driver.OracleDriver";
    private final static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private final static String user = "manju98";
    private final static String password = "manjula";
    private static Connection connection;

   
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(className);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return connection;
    }
}
