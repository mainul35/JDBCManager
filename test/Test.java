import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easyconnect.jdbc.JdbcManager;
import com.easyconnect.jdbc.JdbcManagerService;

public class Test {
    
    public static void fetchDataWithStoredProcedure(){
        String procedureName_getAllUsers = "{call getAllUsers()}";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            List<HashMap<String, Object>> maps = manager.getQueryDataWithStoredProcedure(procedureName_getAllUsers, new ArrayList<>());
            System.out.println("With stored procedure");
            System.out.println("=====================");
            System.out.println(maps.toString());
        } catch (Exception e) {
        }
    }
    
    public static void fetchDataWithPlainSql(){
        String procedureName_getAllUsers = "{call getAllUsers()}";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            List<HashMap<String, Object>> maps = manager.getQueryData("Select * from userdetails;", new ArrayList<>());
            System.out.println("With Plain SQL");
            System.out.println("===============");
            System.out.println(maps.toString());
        } catch (Exception e) {
        }
    }
    
    
    public static void main(String[] args) {
        fetchDataWithPlainSql();
        System.out.println();
        fetchDataWithStoredProcedure();
    }
}
