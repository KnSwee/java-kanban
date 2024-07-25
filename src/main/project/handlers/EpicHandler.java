package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.controller.api.TaskManager;
import project.exceptions.NotFoundException;
import project.models.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            Integer id = getId(path);
            String[] pathMassive = (path.split("/"));
            try {
                id = getId(path);
            } catch (NumberFormatException e) {
                unaviableMethod(exchange);
            }

            switch (method) {
                case "GET":
                    if (id == null) {
                        sendText(exchange, manager.getEpics(), 200);
                        break;
                    } else if (pathMassive.length > 3 && pathMassive[3].equals("subtasks")) {
                        try {
                            sendText(exchange, manager.getSubtasksByEpic(id), 200);
                        } catch (NotFoundException e) {
                            sendText(exchange, "Эпика с id " + id + " не существует", 404);
                            break;
                        }
                    } else if (pathMassive.length == 3) {
                        try {
                            sendText(exchange, manager.getEpicById(id), 200);
                        } catch (NotFoundException e) {
                            sendText(exchange, "Эпика с id " + id + " не существует", 404);
                            break;
                        }
                    }
                case "POST":
                    String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
                    Epic epic = gson.fromJson(jsonPostString, Epic.class);
                    int result = manager.createEpic(epic);
                    sendText(exchange, "Эпик создан с id " + result, 201);
                    break;
                case "DELETE":
                    manager.deleteEpicById(id);
                    sendText(exchange, "Эпик с id " + id + " успешно удален", 200);
                    break;
                default:
                    unaviableMethod(exchange);
            }
        } catch (Throwable e) {
            sendText(exchange, "Server error", 500);
            e.printStackTrace();

        }
    }
}
