
import com.JDBC.JDBCManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author FURIOUS
 */
public class TestJDBCManager {

    public static void main(String[] args) {
        JDBCManager manager = new JDBCManager();
        manager.initMysqlConnection("garciaplumbing2", "root", "");
        String sql = "SELECT * FROM job";
        try {
            ResultSet rs = manager.getQueryData(sql);
            while (rs.next()) {
                System.out.println("jobId " + rs.getString("jobId"));
                System.out.println("customerId " + rs.getString("customerId"));
                System.out.println("jobTypeName " + rs.getString("jobTypeName"));
                System.out.println("dateWorkedOn " + rs.getString("dateWorkedOn"));
            }

//            sql = "SELECT * FROM supplier";
//            rs = manager.getQueryData(sql);
//            while (rs.next()) {                
//                System.err.println("supplierID "+rs.getString("supplierID"));
//                System.err.println("supplierName "+rs.getString("supplierName"));
//            }

            manager.closeConnection();
            sql = "SELECT * FROM supplier";
            rs = manager.getQueryData(sql);
            while (rs.next()) {                
                System.err.println("supplierID "+rs.getString("supplierID"));
                System.err.println("supplierName "+rs.getString("supplierName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestJDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class TestA{
    public static void main(String[] args) {
        try {
            JDBCManager manager = new JDBCManager();
            manager.initMysqlConnection("test", "root", "");
            Object[] row1 = new Object[]{1, "Lopa"};
            Object[] row2 = new Object[]{2, "Siam"};
            Object[] o = new Object[]{row1, row2};
            String[] columnNames = new String[]{"id", "name"};
            String tableName = "testTable";
            System.out.println(manager.insertData(tableName, columnNames, o));
        } catch (SQLException ex) {
            Logger.getLogger(TestA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}