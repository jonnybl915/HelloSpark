package com.theironyard.jdblack;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    //static User user; changing this to a hashmap, then go through
    static HashMap<String, User> users = new HashMap<>();
    static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session(); //pulling info from previous session
                    String username = session.attribute("username");

                    HashMap map = new HashMap();
                    if (username == null){ //changed from user to username
                        return new ModelAndView(map, "login.html");
                    }
                    else {
                        map.put("name", username); //changed from user.name to username
                        map.put("users", userList);
                        return new ModelAndView(map, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String username = request.queryParams("username");
                    User user = users.get(username);
                    if (user == null) {
                        user = new User(username);
                        userList.add(user);
                        users.put(username, user); //added this line
                    }
                    //always create session in the "login" route
                    Session session = request.session();
                    session.attribute("username", username); //acts as a special hashmap to store user info

                    response.redirect("/");
                    return ""; //doesn't have any effect but this is necessary to satisfy the
                }
        );
        Spark.post(
          "/logout",
                (request, response) -> { //removed commented section
                    Session session = request.session();
                    session.invalidate();
                    //user = null;
                    response.redirect("/");
                    return "";
                }
        );
    }
}
