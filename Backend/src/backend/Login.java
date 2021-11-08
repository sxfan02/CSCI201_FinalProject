package backend;

import java.sql.*;


public class Login {
	public static void main(String args[]) throws Exception {
		// post("login", "user2", "123456");
		onLogin("login", "user2", "12345");
		onLogin("login", "user2", "123456");
	}
	
	public static Connection getConnection() {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/testdb";
			String user = "root";
			String pwd = "rootroot";
			
			Connection connection = DriverManager.getConnection(url, user, pwd);
			System.out.println("Connection established");
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void post(String table, String un, String pwd) {
		String sql = "INSERT INTO " + table + " (username, password) VALUES (?, ?)";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, un);
			ps.setString(2, pwd);
			int row = ps.executeUpdate();
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
	
	

}
