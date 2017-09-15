import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easyconnect.jdbc.JdbcManager;
import com.easyconnect.jdbc.JdbcManagerService;

public class Test {

    public static void insertDataWithStoredProcedure(){
        try {
            String procedureName_getAllRoles = "{call addRole(?,?)}";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            ArrayList<String> list = new ArrayList<>();
            list.add("104");
            list.add("NONE");
            String status = manager.insertOrUpdate(procedureName_getAllRoles, list);
            System.out.println("With stored procedure");
            System.out.println("===============");
            System.out.println(status);
            
        } catch (Exception e) {
        }
    }
    
    public static void fetchDataWithStoredProcedure(){
        
        try {
            String procedureName_getAllRoles = "{call getAllRoles()}";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            List<HashMap<String, Object>> maps = manager.getQueryDataWithStoredProcedure(procedureName_getAllRoles, new ArrayList<>());
            System.out.println("With stored procedure");
            System.out.println("=====================");
            for (HashMap<String, Object> hashMap : maps) {
                System.out.println("Role Id = "+hashMap.get("role_id"));
                System.out.println("Role Name = "+hashMap.get("role_name"));
            }
        } catch (Exception e) {
        }
    }
    
    public static void insertDataWithPlainSql(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            ArrayList<String> list = new ArrayList<>();
            list.add("105");
            list.add("NONE");
            String status = manager.insertOrUpdate("INSERT INTO roles(roles.role_id, roles.role_name)VALUES(?,?);", list);
            System.out.println("With Plain SQL");
            System.out.println("===============");
            System.out.println(status);
            
        } catch (Exception e) {
        }
    }
    
    public static void fetchDataWithPlainSql(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            List<HashMap<String, Object>> maps = manager.getQueryData("SELECT * FROM roles;", new ArrayList<>());
            System.out.println("With Plain SQL");
            System.out.println("===============");
            for (HashMap<String, Object> hashMap : maps) {
                System.out.println("Role Id = "+hashMap.get("role_id"));
                System.out.println("Role Name = "+hashMap.get("role_name"));
            }
            
        } catch (Exception e) {
        }
    }
    
    

    public static void main(String[] args) {
        
        insertDataWithStoredProcedure();
        System.out.println();
        fetchDataWithStoredProcedure();
        System.out.println();
        System.out.println("==============================================================");
        System.out.println("==============================================================");
        System.out.println();
        insertDataWithPlainSql();
        System.out.println();
        fetchDataWithPlainSql();
        
    }
}
