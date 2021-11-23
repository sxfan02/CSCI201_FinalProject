package LoginPackage;

import java.util.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;


public class Login {
	public static Connection getConnection() {
		try {
                    String url = "jdbc:mysql://54.193.145.5:3306/CSCI201-Final-Project";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url,"admin", "CSCI201sucks!");
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
                            String hashed = Login.Hash(pwd);
                            if (rs.getString("password").equals(hashed)) {
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
