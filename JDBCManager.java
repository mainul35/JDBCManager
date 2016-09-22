package java.sql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Copyright (c) 2016, Syed Mainul. All rights reserved.
 */
/**
 *
 * @author Mainul35
 */
public class JDBCManager {

    public static final String MYSQL_DRIVER_MANAGER = "com.mysql.jdbc.Driver";
    public static final String SQLITE_DRIVER_MANAGER = "org.sqlite.JDBC";
    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    
    /**
     *
     * Initializes a <code>Connection</code> object to establish a MySql
     * database connection with user provided database location, database name,
     * database user name and database password.
     *
     * @param dbLocation A custom location of the database provided by user.
     * @param dbName Name of the database.
     * @param dbUser Database user name. Default password is "root".
     * @param dbPassword Database password. Default password is "" (Empty).
     *
     */
    public void initMysqlConnection(String dbLocation, String dbName, String dbUser, String dbPassword) {
        try {
            Class.forName(MYSQL_DRIVER_MANAGER);
            con = DriverManager.getConnection(dbLocation + dbName, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Initializes a <code>Connection</code> object to establish a MySql
     * database connection with user provided database name, database user name
     * and database password. This connection uses default MySql database
     * location.
     *
     * @param dbName Name of the database.
     * @param dbUser Database user name. Default password is "root".
     * @param dbPassword Database password. Default password is "" (Empty).
     *
     */
    public void initMysqlConnection(String dbName, String dbUser, String dbPassword) {
        try {
            Class.forName(MYSQL_DRIVER_MANAGER);
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Initializes a <code>Connection</code> object to establish a MySql SQLite
     * database connection with user provided database name. This connection
     * uses default SQLite database location.
     *
     * @param dbName Name of the database.
     *
     */
    public void initSqliteConnection(String dbName) {
        try {
            Class.forName(SQLITE_DRIVER_MANAGER);
            con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Fetches data from the database according to the provided SQL query
     * written in <code>PreparedStatement</code> format.<br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `name` = ? AND `age` = ?;";<br>
     *
     * ArrayList al = getQueryData(sql, new String[]{"name", age}, "John",
     * "24");
     * </code>
     *
     * @param sql The query to be executed.
     * @param columns Columns to be fetched.
     * @param params Query parameters sequentially.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     *
     */
    public ArrayList<HashMap<String, String>> getQueryData(String sql, String[] columns, String... params) {
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            ArrayList<HashMap<String, String>> al = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<>();
                for (int i = 0; i < columns.length; i++) {
                    hm.put(columns[0], rs.getString(columns[0]));
                }
                al.add(hm);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Fetches data from the database according to the provided SQL query
     * written in <code>PreparedStatement</code> format.<br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `name` = ?;";<br>
     *
     * ArrayList al = getQueryData(sql, new String[]{"name", age}, "John");
     * </code>
     *
     * @param sql The query to be executed.
     * @param columns Columns to be fetched.
     * @param param A <code>String</code> query parameter.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     *
     */
    public ArrayList<HashMap<String, String>> getQueryData(String sql, String[] columns, String param) {
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, param);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<HashMap<String, String>> al = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<>();
                for (int i = 0; i < columns.length; i++) {
                    hm.put(columns[i], rs.getString(columns[i]));
                }
                al.add(hm);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Fetches data from the database according to the provided SQL query
     * written in <code>PreparedStatement</code> format.<br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name` FROM `user` WHERE `name` = ?;";<br>
     *
     * ArrayList al = getQueryData(sql, "name", "John");
     * </code>
     *
     * @param sql The query to be executed.
     * @param column A single column to be fetched.
     * @param param A <code>String</code> query parameter.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     *
     */
    public ArrayList<HashMap<String, String>> getQueryData(String sql, String column, String param) {
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, param);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<HashMap<String, String>> al = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put(column, rs.getString(column));
                al.add(hm);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Fetches data from the database according to the provided SQL query.
     * <br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `name` = 'John';";<br>
     *
     * ArrayList al = getQueryData(sql, new String[]{"name", age});
     * </code>
     *
     * @param sql The query to be executed.
     * @param columns Columns to be fetched.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     *
     */
    public ArrayList<HashMap<String, String>> getQueryData(String sql, String[] columns) {
        try {
            pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<HashMap<String, String>> al = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<>();
                if (columns == null) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    columns = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        columns[i] = rsmd.getColumnName(i + 1);
                    }
                }
                for (int i = 0; i < columns.length; i++) {
                    hm.put(columns[i], rs.getString(columns[i]));
                }
                al.add(hm);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Fetches data from the database according to the provided SQL query.
     * <br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `name` = 'John';";<br>
     *
     * ArrayList al = getQueryData(sql, new String[]{"name", age});
     * </code>
     *
     * @param sql The query to be executed.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     *
     */
    public ArrayList<HashMap<String, String>> getQueryData(String sql) {
        try {
            pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<HashMap<String, String>> al = new ArrayList<>();
            String columns[] = null;
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<>();
                if (columns == null) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    columns = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        columns[i] = rsmd.getColumnName(i + 1);
                    }
                }
                for (int i = 0; i < columns.length; i++) {
                    hm.put(columns[i], rs.getString(columns[i]));
                }
                al.add(hm);
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Inserts data into tables based on the provided SQL insertion
     * statement.<br>
     * Example:<br>
     * <code>
     * sql = "INSERT INTO `user`(`firstName`, `lastName`) VALUES(?,?);";<br>
     * insertData(String sql, "Mainul", "Hasan");
     * </code>
     *
     * @param sql The SQL statement.
     * @param values Values to be inserted.
     *
     */
    public void insertData(String sql, String... values) {
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, (Object) values[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Inserts data into tables based on the provided SQL insertion
     * statement.<br>
     * Example:<br>
     * <code>
     * sql = "INSERT INTO `user`(`firstName`, `lastName`) VALUES('Mainul', 'Hasan');";<br>
     * insertData(String sql);
     * </code>
     *
     * @param sql The SQL statement.
     *
     */
    public void insertData(String sql) {
        try {
            Statement stmt = con.createStatement();
            stmt.setQueryTimeout(30);
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Updates data of database table(s) based on the provided SQL update<br>
     * statement and parameters. <br>Example:<br>
     * <code>
     * sql = "UPDATE `user` SET `firstName` = ? WHERE `userId` = ?'";<br>
     * String[] params = new String[]{"Mainul", "1"};<br>
     * updateData(sql, arr);
     * </code>
     *
     * @param sql The SQL statement.
     * @param params The parameters to be set as the replacement of "?" marks.
     *
     */
    public void updateData(String sql, String[] params) {
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Updates data of database table(s) based on the provided SQL update<br>
     * statement and parameters. <br>Example:<br>
     * <code>
     * sql = "UPDATE `user` SET `firstName` = 'Mainul' WHERE `userId` = '1'";<br>
     * updateData(sql);
     * </code>
     *
     * @param sql The SQL statement.
     *
     */
    public void updateData(String sql) {
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Deletes data from database table(s) based on the provided SQL update<br>
     * statement and parameters. <br>Example:<br>
     * <code>
     * sql = "DELETE FROM `user` WHERE `firstName` = ? AND `lastName` = ?'";<br>
     * String[] params = new String[]{"Mainul", "Hasan"};<br>
     * updateData(sql, arr);
     * </code>
     *
     * @param sql The SQL statement.
     * @param params The parameters to be set as the replacement of "?" marks.
     *
     */
    public void deleteData(String sql, String[] params) {
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Deletes data from database table(s) based on the provided SQL update<br>
     * statement and parameters. <br>Example:<br>
     * <code>
     * sql = "DELETE FROM `user` WHERE `userId` = ?";<br>
     * updateData(sql, "1");
     * </code>
     *
     * @param sql The SQL statement.
     * @param param The parameters to be set as the replacement of "?" marks.
     *
     */
    public void deleteData(String sql, String param) {
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, param);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Deletes data from database table(s) based on the provided SQL update<br>
     * statement and parameters. <br>Example:<br>
     * <code>
     * sql = "DELETE FROM `user` WHERE `userId` = '1';";<br>
     * updateData(sql);
     * </code>
     *
     * @param sql The SQL statement.
     *
     */
    public void deleteData(String sql) {
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
