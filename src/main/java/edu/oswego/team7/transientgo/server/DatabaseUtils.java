/*
 * Copyright (C) 2016 Shakhar Dasgupta <sdasgupt@oswego.edu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.oswego.team7.transientgo.server;

import com.heroku.sdk.jdbc.DatabaseUrl;
import edu.oswego.transientgo.core.Leader;
import edu.oswego.transientgo.core.Leaderboard;
import edu.oswego.transientgo.core.User;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
class DatabaseUtils {
    
    static Map<String, Boolean> createUsersTable() {
        Map map = new HashMap<>();
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id text, name text, score int, transient_ivorns text[]);");
            map.put("success", true);
        } catch (SQLException | URISyntaxException ex) {
            map.put("success", false);
        }
        return map;
    }
    
    static Map<String, Boolean> dropUsersTable() {
        Map map = new HashMap<>();
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE users");
            map.put("success", true);
        } catch (SQLException | URISyntaxException ex) {
            map.put("success", false);
        }
        return map;
    }
    
    static Map<String, Boolean> createUser(String id, String name) {
        Map map = new HashMap<>();
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO users(user_id, name, score, transient_ivorns) VALUES(?, ?, 0, '{}')");
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            if(pstmt.executeUpdate() > 0)
                map.put("id", id);
            else
                map.put("success", false);
            
        } catch (SQLException | URISyntaxException ex) {
            map.put("success", false);
        }
        return map;
    }

    static User getUserByID(String id) {
        User user = null;
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT user_id, name, score, transient_ivorns FROM users WHERE user_id = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            user = new User(rs.getString("user_id"), rs.getString("name"), rs.getInt("score"), new ArrayList<>(Arrays.asList((String[])rs.getArray("transient_ivorns").getArray())));
        } catch (SQLException | URISyntaxException ex) {}
        return user;
    }
    
    static Leaderboard getLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT name, score FROM users ORDER BY score DESC");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                leaderboard.addLeader(new Leader(rs.getString("name"), rs.getInt("score")));
            }
        } catch (SQLException | URISyntaxException ex) {
        }
        return leaderboard;
    }
    
    static Map<String, Boolean> updateUserScore(String id, int score) {
        Map map = new HashMap<>();
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET score = ? WHERE user_id = ?");
            pstmt.setInt(1, score);
            pstmt.setString(2, id);
            if(pstmt.executeUpdate() > 0)
                map.put("success", true);
            else
                map.put("success", false);
            
        } catch (SQLException | URISyntaxException ex) {
            map.put("success", false);
        }
        return map;
    }
    
    static Map<String, Boolean> addUserTransientIVORN(String id, String ivorn) {
        Map map = new HashMap<>();
        try(Connection connection = DatabaseUrl.extract().getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET transient_ivorns = array_append(transient_ivorns, ?::text) WHERE user_id = ?");
            pstmt.setString(1, ivorn);
            pstmt.setString(2, id);
            if(pstmt.executeUpdate() > 0)
                map.put("success", true);
            else
                map.put("success", false);
            pstmt.close();
            
        } catch (SQLException | URISyntaxException ex) {
            map.put("error", ex.getMessage());
        }
        return map;
    }
}
