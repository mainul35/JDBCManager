# JDBCManager
A Java Database Connectivity library to connect and make database transaction quickly and easily.

## Code Examples

### Insert data with Stored Procedure
            
            ............................
            ............................
            String procedureName_getAllRoles = "{call addRole(?,?)}";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            ArrayList<String> list = new ArrayList<>();
            list.add("114");
            list.add("NONE");
            String status = manager.insertOrUpdate(procedureName_getAllRoles, list);
            System.out.println("With stored procedure");
            System.out.println("===============");
            System.out.println(status);
            ............................
            ............................

### Fetch data with Stored Procedure

            ............................
            ............................
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
            ............................
            ............................
            
### Insert data with plain SQL
            
            ............................
            ............................
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            ArrayList<String> list = new ArrayList<>();
            list.add("115");
            list.add("NONE");
            String status = manager.insertOrUpdate("INSERT INTO roles(roles.role_id, roles.role_name)VALUES(?,?);", list);
            System.out.println("With Plain SQL");
            System.out.println("===============");
            System.out.println(status);
            ............................
            ............................
            
### Fetch data with plain SQL
            
            ............................
            ...........................
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
            ............................
            ............................
