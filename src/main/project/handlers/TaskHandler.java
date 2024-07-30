package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import project.controller.api.TaskManager;
import project.exceptions.IntersectionException;
import project.exceptions.NotFoundException;
import project.models.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }


    @Override
    protected void processDelete(HttpExchange exchange, String path, Integer id) throws IOException {
        manager.deleteTaskById(id);
        sendText(exchange, "Задача с id " + id + " успешно удалена", 200);
    }

    @Override
    protected void processPost(HttpExchange exchange, String path, Integer id) throws IOException {
        String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
        Task task = gson.fromJson(jsonPostString, Task.class);
        if (task.getID() == null) {
            try {
                int result = manager.createTask(task);
                sendText(exchange, "Задача создана с id " + result, 201);
            } catch (IntersectionException e) {
                sendText(exchange, e.getMessage(), 406);
            }
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
    }

    @Override
    protected void processGet(HttpExchange exchange, String path, Integer id) throws IOException {
        if (path.split("/").length <= 2) {
            sendText(exchange, manager.getTasks(), 200);
            return;
        }
        try {
            sendText(exchange, manager.getTaskById(id), 200);
        } catch (NotFoundException e) {
            sendText(exchange, "Задачи с id " + id + " не существует", 404);
        }
    }

}

