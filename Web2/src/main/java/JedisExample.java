import static spark.Spark.get;
import io.lettuce.core.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import redis.clients.jedis.Jedis;
//package spark.examples.session;

import static spark.Spark.get;
import static spark.Spark.post;

public class JedisExample {
    private static final String SESSION_NAME = "username";

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        get("/", (request, response) -> {
            String name = request.session().attribute(SESSION_NAME);
            if (name == null) {
                return "<html><body>What's your name?: <form action=\"/entry\" method=\"POST\"><input type=\"text\" name=\"name\"/><input type=\"submit\" value=\"go\"/></form></body></html>";
            } else {
                jedis.set("events/city/rome", name);
                get("/hello", (req, res) -> jedis.get("events/city/rome"));
                return  0;
            }
        });

        post("/entry", (request, response) -> {
            String name = request.queryParams("name");
            if (name != null) {
                request.session().attribute(SESSION_NAME, name);
            }
            response.redirect("/");
            return null;
        });


        get("/clear", (request, response) -> {
            request.session().removeAttribute(SESSION_NAME);
            response.redirect("/");
            return null;
        });
    }
}
    /*public static void main(String[] args) throws Exception {

        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("events/city/rome", "32,15,223,828");
        String cachedResponse = jedis.get("events/city/rome");
        get("/hello", (req, res) -> jedis.get("events/city/rome"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String a = reader.readLine();

        get("/web", (req, res) -> jedis.get("events/city/rome"));
        get("/web", (req, res) -> );

        System.out.println(cachedResponse);
    }
}*/
/*public class HelloWorld {
    public static void main(String[] args) {
       // get("/hello", (req, res) -> "Hello World");
        RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        syncCommands.set("key", "Hello, Redis!");

        connection.close();
        redisClient.shutdown();
    }
}*/


