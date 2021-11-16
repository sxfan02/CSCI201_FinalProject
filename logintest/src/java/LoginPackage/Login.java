package LoginPackage;

import java.sql.*;


public class Login {
	public static Connection getConnection() {
		try {
                    String url = "jdbc:mysql://localhost:3306/logintest";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url,"root","root");
                    System.out.println("Connection established");
                    return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void post(String table, String un, String pwd, String email) {
		String sql = "INSERT INTO " + table + " (username, password, email) VALUES (?, ?, ?)";
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, un);
			ps.setString(2, pwd);
                        ps.setString(3, email);
			ps.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	public static boolean onLogin(String table, String un, String pwd) {
		String sql = "SELECT password FROM " + table + " WHERE username=?";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, un);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				if (rs.getString("password").equals(pwd)) {
					System.out.println("Login success");
					return true;
				}
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}
		
		System.out.println("Login fail");
		return false;
	}
        
        public static boolean onRegistration(String table, String un)
        {
            String sql = "SELECT * FROM " + table + " WHERE username=?";
            try(Connection conn = getConnection(); 
                    PreparedStatement ps = conn.prepareStatement(sql);){
                ps.setString(1, un);
                //checking if username already exists in database
                ResultSet rs = ps.executeQuery();
                if (rs.next() == false)
                {
                   return true;
                }
                else {
                    return false;
                }
            } catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
            }
            return false;
        }
       
}
