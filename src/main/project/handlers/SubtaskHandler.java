package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.exceptions.IntersectionException;
import project.exceptions.NotFoundException;
import project.models.Subtask;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Integer id = getId(path);
        try {
            id = getId(path);
        } catch (NumberFormatException e) {
            unaviableMethod(exchange);
        }

        switch (method) {
            case "GET":
                if (path.split("/").length <= 2) {
                    sendText(exchange, manager.getSubtasks(), 200);
                    break;
                }
                try {
                    sendText(exchange, manager.getSubtaskById(id), 200);
                } catch (NotFoundException e) {
                    sendText(exchange, "Задачи с id" + id + " не существует", 404);
                }
                break;
            case "POST":
                String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
                Subtask subtask = gson.fromJson(jsonPostString, Subtask.class);
                if (subtask.getID() == null) {
                    try {
                        int result = manager.createSubtask(subtask);
                        sendText(exchange, "Подзадача создана с id " + result, 201);
                    } catch (IntersectionException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                    break;
                } else {
                    try {
                        manager.updateSubtask(subtask);
                        sendText(exchange,"Подзадача обновлена", 201);
                    } catch (NotFoundException e) {
                        sendText(exchange, "Подзадачи с id" + id + " не существует", 404);
                    } catch (IntersectionException e) {
                        sendText(exchange, e.getMessage(), 406);
                    }
                }
            case "DELETE":
                manager.deleteSubtaskById(id);
                sendText(exchange, "Задача с id " + id + " успешно удалена",200);
                break;
            default:
                unaviableMethod(exchange);
        }
    }
}
