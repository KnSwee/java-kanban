package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.controller.api.TaskManager;
import project.exceptions.IntersectionException;
import project.exceptions.NotFoundException;
import project.models.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            Integer id = null;
            try {
                id = getId(path);
            } catch (NumberFormatException e) {
                unaviableMethod(exchange);
            }

            switch (method) {
                case "GET":
                    if (path.split("/").length <= 2) {
                        sendText(exchange, manager.getTasks(), 200);
                        break;
                    }
                    try {
                        sendText(exchange, manager.getTaskById(id), 200);
                    } catch (NotFoundException e) {
                        sendText(exchange, "Задачи с id " + id + " не существует", 404);
                    }
                    break;
                case "POST":
                    String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
                    Task task = gson.fromJson(jsonPostString, Task.class);
                    if (task.getID() == null) {
                        try {
                            int result = manager.createTask(task);
                            sendText(exchange, "Задача создана с id " + result, 201);
                        } catch (IntersectionException e) {
                            sendText(exchange, e.getMessage(), 406);
                        }
                        break;
                    } else {
                        try {
                            manager.updateTask(task);
                            sendText(exchange, "Задача обновлена", 201);
                        } catch (NotFoundException e) {
                            sendText(exchange, "Задачи с id " + id + " не существует", 404);
                        } catch (IntersectionException e) {
                            sendText(exchange, e.getMessage(), 406);
                        }
                    }
                case "DELETE":
                    manager.deleteTaskById(id);
                    sendText(exchange, "Задача с id " + id + " успешно удалена", 200);
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

