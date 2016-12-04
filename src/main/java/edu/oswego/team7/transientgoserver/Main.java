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
package edu.oswego.team7.transientgoserver;

import com.google.gson.Gson;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import com.heroku.sdk.jdbc.DatabaseUrl;
import java.net.URISyntaxException;
import java.sql.Connection;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class Main {
    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));
        Gson gson = new Gson();
        get("/", (request, response) -> "Welcome to Transient-Go Server");
        get("/create", (request, response) -> createUsersTable(), gson::toJson);
        get("/drop", (request, response) -> dropUsersTable(), gson::toJson);
        get("/user/:id", (request, response) -> getUserByID(request.params(":id")), gson::toJson);
        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
            response.type("application/json");
        });
    }
    public static String createUsersTable() {
        try {
            Connection connection = DatabaseUrl.extract().getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users(User_ID text, Name text, Score int, Transient_IVORNs text[]);");
            return "Table Users Created Successfully.";
        } catch (SQLException | URISyntaxException ex) {
            return "Execption Occured: Table Users Not Created.";
        }
    }
    
    public static String dropUsersTable() {
        try {
            Connection connection = DatabaseUrl.extract().getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE Users");
            return "Table Users Successfully Dropped.";
        } catch (SQLException | URISyntaxException ex) {
            return "Exception Occured: Table Users Not Dropped.";
        }
    }

    public static ArrayList<User> getUserByID(String id) {
        ArrayList<User> users = new ArrayList<>();
        try {
            Connection connection = DatabaseUrl.extract().getConnection();
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE User_ID = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString("User_ID"), rs.getString("Name"), rs.getInt("Score"), new ArrayList<>(Arrays.asList((String[])rs.getArray("Transient_IVORNs").getArray()))));
            }
        } catch (SQLException | URISyntaxException ex) {
        }
        return users;
    }
}
