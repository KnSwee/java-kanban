package project.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.controller.api.TaskManager;
import project.util.Managers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class BaseHttpHandler implements HttpHandler {

    protected static final Charset UTF_8 = StandardCharsets.UTF_8;
    protected TaskManager manager;
    protected Gson gson = Managers.getGson();

    protected BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            Integer id = getValidId(exchange, path);

            switch (method) {
                case "GET":
                    processGet(exchange, path, id);
                    break;
                case "POST":
                    processPost(exchange, path, id);
                    break;
                case "DELETE":
                    processDelete(exchange, path, id);
                    break;
                default:
                    unaviableMethod(exchange);
            }
        } catch (Exception e) {
            sendText(exchange, "Server error", 500);
            e.printStackTrace();
        }
    }

    protected void processDelete(HttpExchange exchange, String path, Integer id) throws IOException {
        unaviableMethod(exchange);
    }

    protected void processPost(HttpExchange exchange, String path, Integer id) throws IOException {
        unaviableMethod(exchange);
    }

    protected void processGet(HttpExchange exchange, String path, Integer id) throws IOException {
        unaviableMethod(exchange);
    }

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendText(HttpExchange h, Object o, int code) throws IOException {
        sendText(h, gson.toJson(o), code);
    }

    protected static Integer getId(String path) {

        List<String> pathList = Arrays.stream(path.split("/")).toList();
        Integer id = null;
        if (pathList.size() > 2) {
            id = Integer.parseInt(path.split("/")[2]);
        }
        return id;
    }

    protected Integer getValidId(HttpExchange exchange, String path) throws IOException {
        try {
            return getId(path);
        } catch (NumberFormatException e) {
            unaviableMethod(exchange);
        }
        return null;
    }


    protected void unaviableMethod(HttpExchange exchange) throws IOException {
        sendText(exchange, "Method Not Allowed", 405);
    }

}