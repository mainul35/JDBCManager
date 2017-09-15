# JDBCManager
A Java Database Connectivity library to connect and make database transaction quickly and easily.

## Code Examples

### Fetch data with plain SQL

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            List<HashMap<String, Object>> maps = manager.getQueryData("Select * from userdetails;", new ArrayList<>());
            System.out.println("With Plain SQL");
            System.out.println("===============");
            for (HashMap<String, Object> hashMap : maps) {
                System.out.println("Name = "+hashMap.get("name"));
                System.out.println("Email = "+hashMap.get("email"));
                System.out.println("Username = "+hashMap.get("username"));
                System.out.println("Phone No = "+hashMap.get("phone_No"));
            }

### Fetch data with Stored Procedure

            String procedureName_getAllUsers = "{call getAllUsers()}";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            JdbcManager manager = new JdbcManagerService(con);
            List<HashMap<String, Object>> maps = manager.getQueryDataWithStoredProcedure(procedureName_getAllUsers, new ArrayList<>());
            System.out.println("With stored procedure");
            System.out.println("=====================");
            for (HashMap<String, Object> hashMap : maps) {
                System.out.println("Name = "+hashMap.get("name"));
                System.out.println("Email = "+hashMap.get("email"));
                System.out.println("Username = "+hashMap.get("username"));
                System.out.println("Phone No = "+hashMap.get("phone_No"));
            }
