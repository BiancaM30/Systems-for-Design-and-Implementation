package swim.persistance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {
    private Properties props;
    public JdbcUtils(Properties props){
        this.props=props;
    }
    private static Connection instance=null;
    private Connection getNewConnection(){
        System.out.println("JdbcUtilssssssssssssssssssssssssssssss");
        String driver=props.getProperty("swim.jdbc.driver");
        String url=props.getProperty("swim.jdbc.url");
//        String user=props.getProperty("swim.jdbc.user");
//        String pass=props.getProperty("swim.jdbc.pass");
        Connection con=null;
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        try {
            System.out.println("getConnection()!!!!!!!!!!!!!!");
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }

}
