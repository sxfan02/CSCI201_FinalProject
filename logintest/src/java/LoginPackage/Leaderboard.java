package LoginPackage;

import java.sql.*;


public class Leaderboard {

    public static Connection getConnection() {
		try {
                    String url = "jdbc:mysql://54.193.145.5:3308/CSCI201-Final-Project";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url,"admin","CSCI201sucks!");
                    System.out.println("Connection established");
                    return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

    public static void post(String table, String un, String wins, String losses)
    {
        String sql = "INSERT INTO " + table + " (username, wins, losses) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);){
                    ps.setString(1, un);
                    ps.setString(2, wins);
                    ps.setString(3, losses);
                    ps.executeUpdate();
            } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
            }
    }

    public static void updateWinLoss(String table, String un, boolean won) {
        try {
            Connection conn = getConnection();
            String sql;
            if (won == false)
            {
                sql = "UPDATE " + table + " SET losses=? WHERE username=?";
            }
            else 
            {
                sql = "UPDATE " + table + " SET wins=? WHERE username=?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            if (won == false)
            {
                Integer losses = getLosses(table, un);
                if (losses != null)
                {
                    --losses;
                    ps.setString(1 , losses.toString());
                    ps.setString(2, un);
                    int row = ps.executeUpdate();
                }
            }
            else 
            {
                Integer wins = getWins(table, un);
                if (wins != null)
                {
                    ++wins;
                    ps.setString(1 , wins.toString());
                    ps.setString(2, un);
                    int row = ps.executeUpdate();
                }
            }

            

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } 
    }

    public static Integer getLosses(String table, String un)
    {
        String sql = "SELECT losses FROM " + table + " WHERE username=?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, un);
            ResultSet rs = ps.executeQuery();
            Integer losses;
            if(rs.next())
            {
                losses = Integer.parseInt(rs.getString("losses"));
                return losses;
            }
        }catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public static Integer getWins(String table, String un)
    {
        String sql = "SELECT wins FROM " + table + " WHERE username=?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, un);
            ResultSet rs = ps.executeQuery();
            Integer wins;
            if(rs.next())
            {
                wins = Integer.parseInt(rs.getString("wins"));
                return wins;
            }
        }catch (SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }
            
}

