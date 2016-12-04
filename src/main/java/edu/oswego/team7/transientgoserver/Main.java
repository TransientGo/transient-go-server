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
import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.delete;
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
        get("/v1", (request, response) -> "Welcome to Transient-Go Server");
        post("/v1/users", (request, response) -> DatabaseUtils.createUsersTable(), gson::toJson);
        delete("/v1/users", (request, response) -> DatabaseUtils.dropUsersTable(), gson::toJson);
        put("/v1/user/new/:id/name/:name", (request, response) -> DatabaseUtils.createUser(request.params(":id"), request.params(":name")), gson::toJson);
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
