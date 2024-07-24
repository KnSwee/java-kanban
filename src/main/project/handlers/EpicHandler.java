package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.models.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
                } else if (manager.getEpicById(id) == null) {
                    sendText(exchange, "Эпика с id" + id + " не существует",404);
                    break;
                } else if (pathMassive.length > 3 && pathMassive[3].equals("subtasks")) {
                    sendText(exchange, manager.getSubtasksByEpic(id), 200);
                    break;
                } else {
                    sendText(exchange, manager.getEpicById(id), 200);
                    break;
                }
            case "POST":
                String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
                Epic epic = gson.fromJson(jsonPostString, Epic.class);
                if (epic.getID() == null) {
                    int result = manager.createEpic(epic);
                    sendText(exchange, "Эпик создан с id " + result, 201);
                    break;
                }
            case "DELETE":
                manager.deleteEpicById(id);
                sendText(exchange, "Эпик с id " + id + " успешно удален",200);
                break;
            default:
                unaviableMethod(exchange);
        }
    }
}
