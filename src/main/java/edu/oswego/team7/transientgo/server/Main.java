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

import com.google.gson.Gson;
import java.util.Base64;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

/**
 *
 * @author Shakhar Dasgupta <sdasgupt@oswego.edu>
 */
public class Main {
    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));
        Gson gson = new Gson();
        before("/v1/user/:id", (request, response) -> {
            String auth = request.headers("Authorization");
            if(auth == null)
                halt(401, "Authentication Error");
            String userpass = new String(Base64.getDecoder().decode(auth.split(" ", 2)[1]));
            String[] tokens = userpass.split(":", 2);
            String user = tokens[0];
            String pass = tokens[1];
            String id = request.params(":id");
            System.out.println("user: " + user + " pass: " + pass + " id:" + id);
            if(!(DatabaseUtils.authenticate(user, pass) && user.equals(id))) {
                halt(401, "Authentication Error");
            }
        });
        post("/v1/user", (request, response) -> DatabaseUtils.createUser(request.queryParams("id"), request.queryParams("pass"), request.queryParams("name")), gson::toJson);
        get("/v1/user/:id", (request, response) -> DatabaseUtils.getUserByID(request.params(":id")), gson::toJson);
        put("/v1/user/:id/score/:score", (request, response) -> DatabaseUtils.updateUserScore(request.params(":id"), Integer.parseInt(request.params(":score"))), gson::toJson);
        put("/v1/user/:id/transient/:ivorn", (request, response) -> DatabaseUtils.addUserTransientIVORN(request.params(":id"), request.params(":ivorn")), gson::toJson);
        get("/v1/leaderboard", (request, response) -> DatabaseUtils.getLeaderboard(), gson::toJson);
        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
            response.type("application/json");
        });
    }
}
