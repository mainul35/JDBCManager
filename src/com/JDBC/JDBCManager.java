package com.JDBC;

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

/**
 *
 * @author Syed Mainul Hasan
 * @version     1.03
 */
public class JDBCManager {

    public static final String MYSQL_DRIVER_MANAGER = "com.mysql.jdbc.Driver";
    public static final String SQLITE_DRIVER_MANAGER = "org.sqlite.JDBC";
    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static String dbName = null;
    private static String connectionType = null;
    private static String dbUser = null;
    private static String dbPassword = null;
    private static String dbLocation = null;

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
        JDBCManager.dbLocation = dbLocation;
        JDBCManager.dbName = dbName;
        JDBCManager.dbPassword = dbPassword;
        JDBCManager.dbUser = dbUser;
        JDBCManager.connectionType = "mysql";
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
        JDBCManager.dbName = dbName;
        JDBCManager.dbPassword = dbPassword;
        JDBCManager.dbUser = dbUser;
        JDBCManager.connectionType = "mysql";
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
        JDBCManager.dbName = dbName;
        JDBCManager.connectionType = "sqlite";
    }

    private void initConnection() {
        try {
            if ("sqlite".equals(JDBCManager.connectionType)) {
                Class.forName(SQLITE_DRIVER_MANAGER);
                con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            } else if ("mysql".equals(JDBCManager.connectionType) && JDBCManager.dbLocation == null) {
                Class.forName(MYSQL_DRIVER_MANAGER);
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, dbUser, dbPassword);
            } else if ("mysql".equals(JDBCManager.connectionType) && JDBCManager.dbLocation != null) {
                Class.forName(MYSQL_DRIVER_MANAGER);
                con = DriverManager.getConnection(dbLocation + dbName, dbUser, dbPassword);
            } else {
                throw new Exception("Connection could not be established.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
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
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            ArrayList<HashMap<String, String>> al = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<>();
                for (String column : columns) {
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
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, param);
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
                for (String column : columns) {
                    hm.put(column, rs.getString(column));
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
     * Fetches data from the database according to the provided SQL query and
     * query parameter.
     * <br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `age` = ?;";<br>
     *
     * ArrayList al = getQueryData(sql, "20");
     * </code>
     *
     * @param sql The query to be executed.
     * @param param Value to be matched.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     *
     */
    public ArrayList<HashMap<String, String>> getQueryData(String sql, String param) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, param);
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
     * Fetches data from the database according to the provided SQL query.
     * <br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `name` = 'John';";<br>
     *
     * ArrayList al = getQueryData(sql);
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
        initConnection();
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
                for (String column : columns) {
                    hm.put(column, rs.getString(column));
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
     * @param params Values to be inserted.
     *
     */
    public void insertData(String sql, String... params) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, (Object) params[i]);
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
     * @return <code>boolean</code> value based on success or failure of inserting data.
     *
     */
    public boolean insertData(String sql) {
        initConnection();
        try {
            Statement stmt = con.createStatement();
            stmt.setQueryTimeout(30);
            int i = stmt.executeUpdate(sql);
            if(i>0){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
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
     * @return <code>boolean</code> value based on success or failure of updating data.
     *
     */
    public boolean updateData(String sql, String[] params) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
     * @return <code>boolean</code> value based on success or failure of updating data.
     *
     */
    public boolean updateData(String sql) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
     * @return <code>boolean</code> value based on success or failure of deleting data.
     *
     */
    public boolean deleteData(String sql, String[] params) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
     * @return <code>boolean</code> value based on success or failure of deleting data.
     *
     */
    public boolean deleteData(String sql, String param) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setObject(1, param);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
     * @param sql The SQL statement to be executed.
     * @return <code>boolean</code> value based on success or failure of deleting data.
     *
     */
    public boolean deleteData(String sql) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Creates a table based on the SQL table creation statement.
     *
     * @param sql The SQL statement to be executed.
     * @return <code>boolean</code> value based on success or failure of creating table.
     */
    public boolean createTable(String sql) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Drops a table based on the SQL table creation statement.
     *
     * @param sql The SQL statement to be executed.
     * @return <code>boolean</code> value based on success or failure of dropping a table.
     */
    public boolean dropTable(String sql) {
        initConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
