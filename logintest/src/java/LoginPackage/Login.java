
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class Login {
	
	public static Connection getConnection() {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/testdb";
			String user = "root";
			String pwd = "root";
			
			Connection connection = DriverManager.getConnection(url, user, pwd);
			System.out.println("Connection established");
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String Hash(String pwd) {
		MessageDigest md;
		StringBuilder sb = new StringBuilder();
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
			for (byte b : encodedhash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//bytes to hex
		return sb.toString();
		
	}
	public static void post(String table, String un, String pwd) {
		String sql = "INSERT INTO " + table + " (username, password) VALUES (?, ?)";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, un);
			ps.setString(2, Hash(pwd));
			//System.out.println(Hash(pwd));
			int row = ps.executeUpdate();
			Leaderboard.post("leaderboard", un, "0", "0");
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
				if ((rs.getString("password")).equals(Hash(pwd))) {
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
	//Function checks if there exists a user in the database
	public static boolean checkUserName(String table, String un) {
		String sql = "SELECT username FROM " + table;
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);){
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if ((rs.getString("username")).equals(un)) {
					//System.out.println("Username already exists");
					return true;
				}
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}
		return false;
	}

}
