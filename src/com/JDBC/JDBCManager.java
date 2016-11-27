package com.JDBC;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Syed Mainul Hasan
 * @version 1.03
 */
public class JDBCManager {

    public static final String MYSQL_DRIVER_MANAGER = "com.mysql.jdbc.Driver";
    public static final String SQLITE_DRIVER_MANAGER = "org.sqlite.JDBC";
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private String dbName = null;
    private String connectionType = null;
    private String dbUser = null;
    private String dbPassword = null;
    private String dbLocation = null;

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
        this.dbLocation = dbLocation;
        this.dbName = dbName;
        this.dbPassword = dbPassword;
        this.dbUser = dbUser;
        this.connectionType = "mysql";
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
        this.dbName = dbName;
        this.dbPassword = dbPassword;
        this.dbUser = dbUser;
        this.connectionType = "mysql";
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
        this.dbName = dbName;
        this.connectionType = "sqlite";
    }

    private void initConnection() {
        try {
            if ("sqlite".equals(this.connectionType)) {
                Class.forName(SQLITE_DRIVER_MANAGER);
                con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            } else if ("mysql".equals(this.connectionType) && this.dbLocation == null) {
                Class.forName(MYSQL_DRIVER_MANAGER);
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, dbUser, dbPassword);
            } else if ("mysql".equals(this.connectionType) && this.dbLocation != null) {
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
     * Fetches data from the database according to the provided SQL query and
     * query parameter.
     * <br>
     * Example:<br>
     * <code>
     * String sql = "SELECT `name`, `age` FROM `user` WHERE `age` = ?;";<br>
     *
     * ArrayList al = getQueryData(sql, new Integer(20));
     * </code>
     *
     * @param sql The query to be executed.
     * @param params Value to be matched.
     *
     * @return An <code>ArrayList</code> object containing a
     * <code>HashMap</code> object with the table column names as keys and cell
     * values as values.
     * @throws java.sql.SQLException
     *
     */
    public ResultSet getQueryData(String sql, Object... params) throws SQLException {
        if (con == null) {
            initConnection();
        }
        pstmt = con.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        ResultSet rs = pstmt.executeQuery();
        return rs;
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
     * @throws java.sql.SQLException
     *
     */
    public ResultSet getQueryData(String sql) throws SQLException {
        if (con == null) {
            initConnection();
        }
        pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        return rs;
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
     * @param tableName Table name to insert values.
     * @param colNames Column names of a table where values will be inserted.
     * @param valueSets To enable multiple insertions in multiple columns.
     * @return
     * @throws java.sql.SQLException
     *
     */
    public boolean insertData(String tableName, String[] colNames, Object[]... valueSets) throws SQLException {
        if (con == null) {
            initConnection();
        }
        String sql = "insert into " + tableName + "(";
        for (int i = 0; i < colNames.length; i++) {
            if ((i + 1) != colNames.length) {
                sql += "`" + colNames[i] + "`,";
            } else {
                sql += "`" + colNames[i] + "`)";
            }
        }
        sql += " VALUES(";
        for (int i = 0; i < colNames.length; i++) {
            if ((i + 1 != colNames.length)) {
                sql += "?,";
            } else {
                sql += "?);";
            }
        }
        System.err.println(sql);
        pstmt = con.prepareStatement(sql);
        int i = 0;
        for (Object[] valueSet : valueSets) {
            pstmt.setObject(++i, valueSet);
            pstmt.executeUpdate();
        }
        return  true;
    }

    /**
     * Updates data of database table(s) based on the provided SQL update<br>
     * statement and parameters. <br>Example:<br>
     * <code>
     * sql = "UPDATE `user` SET `firstName` = ? WHERE `userId` = ?'";<br>
     * updateData(sql, "Mainul", "1");
     * </code>
     *
     * @param sql The SQL statement.
     * @param sqlParams update parameter values.
     * @return <code>boolean</code> value based on success or failure of
     * updating data.
     * @throws java.sql.SQLException
     *
     */
    public boolean updateData(String sql, Object... sqlParams) throws SQLException {
        if (con == null) {
            initConnection();
        }
        pstmt = con.prepareStatement(sql);
        for (int i = 0; i < sqlParams.length; i++) {
            pstmt.setObject(i + 1, sqlParams[i]);
        }
        return pstmt.executeUpdate() > 0;

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
     * @return <code>boolean</code> value based on success or failure of
     * deleting data.
     * @throws java.sql.SQLException
     *
     */
    public boolean deleteData(String sql, String[] sqlParams) throws SQLException {
        if (con == null) {
            initConnection();
        }
        pstmt = con.prepareStatement(sql);
        for (int i = 0; i < sqlParams.length; i++) {
            pstmt.setObject(i + 1, sqlParams[i]);
        }
        return pstmt.executeUpdate() > 0;
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
     * @return <code>boolean</code> value based on success or failure of
     * deleting data.
     * @throws java.sql.SQLException
     *
     */
    public boolean deleteData(String sql, String sqlParam) throws SQLException {
        if (con == null) {
            initConnection();
        }
        pstmt = con.prepareStatement(sql);
        pstmt.setObject(1, sqlParam);
        return pstmt.executeUpdate() > 0;
    }

    /**
     * Checks if a value exists in a table column.
     *
     * @param sql The SQL statement to be executed.
     * @param params values to be matched with.
     * @return <code>boolean</code> value based on success or failure upon
     * checking data.
     * @throws java.sql.SQLException
     *
     */
    public boolean contains(String sql, Object... sqlParams) throws SQLException {
        if (con == null) {
            initConnection();
        }
        pstmt = con.prepareStatement(sql);
        for (int i = 0; i < sqlParams.length; i++) {
            pstmt.setObject(i + 1, sqlParams[i]);
        }
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    /**
     * Creates a table based on the SQL table creation statement.
     *
     * @param tableName
     * @return <code>boolean</code> value based on success or failure of
     * creating table.
     * @throws java.sql.SQLException
     */
    public boolean createTable(String tableName) throws SQLException {
        if (con == null) {
            initConnection();
        }
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName;
        pstmt = con.prepareStatement(sql);
        return pstmt.executeUpdate() > 0;
    }

    /**
     * Drops a table based on the SQL table creation statement.
     *
     * @param tableName
     * @return <code>boolean</code> value based on success or failure of
     * dropping a table.
     * @throws java.sql.SQLException
     */
    public boolean dropTable(String tableName) throws SQLException {
        if (con == null) {
            initConnection();
        }
        String sql = "DROP TABLE IF EXISTS '" + tableName + "'";
        pstmt = con.prepareStatement(sql);
        return pstmt.executeUpdate() > 0;
    }

    /**
     *
     * @return boolean value depending on closing state of the connection.
     * @throws SQLException
     */
    public boolean closeConnection() throws SQLException {
        if (this.con != null) {
            this.con.close();
            this.con = null;
            return true;
        } else {
            return false;
        }
    }
}
