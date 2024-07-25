package project.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import project.controller.api.TaskManager;
import project.handlers.adapters.DurationAdapter;
import project.handlers.adapters.LocalDateTimeAdapter;
import project.models.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BaseHttpHandler {

    protected static final Charset UTF_8 = StandardCharsets.UTF_8;
    protected TaskManager manager;
    protected Gson gson = new Gson().newBuilder().setPrettyPrinting().serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).create();

    protected BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
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

    protected void unaviableMethod(HttpExchange exchange) throws IOException {
        sendText(exchange, "Такого метода нет :(", 400);
    }

    public static void main(String[] args) {
        Gson gson1 = new Gson().newBuilder().setPrettyPrinting().serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
        System.out.println(gson1.toJson(new Task("BaseTask", "BaseDescription", 10, LocalDateTime.now())));

    }
}