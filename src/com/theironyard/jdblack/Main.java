package com.theironyard.jdblack;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap map = new HashMap();
                    if (user == null){
                        return new ModelAndView(map, "login.html");
                    }
                    else {
                        map.put("name", user.name);
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
                    user = new User(username);
                    userList.add(user);
                    response.redirect("/");
                    return ""; //doesn't have any effect but this is necessary to satisfy the
                }
        );
        Spark.post(
          "/logout",
                (request, response) -> {
                    user = null;
                    response.redirect("/");
                    return "";
                }
        );
    }
}
