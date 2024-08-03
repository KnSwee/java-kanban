package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import project.controller.api.TaskManager;
import project.exceptions.IntersectionException;
import project.exceptions.NotFoundException;
import project.models.Subtask;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler {


    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void processDelete(HttpExchange exchange, String path, Integer id) throws IOException {
        manager.deleteSubtaskById(id);
        sendText(exchange, "Задача с id " + id + " успешно удалена", 200);
    }

    @Override
    protected void processPost(HttpExchange exchange, String path, Integer id) throws IOException {
        String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
        Subtask subtask = gson.fromJson(jsonPostString, Subtask.class);
        if (subtask.getID() == null) {
            try {
                int result = manager.createSubtask(subtask);
                sendText(exchange, "Подзадача создана с id " + result, 201);
            } catch (IntersectionException e) {
                sendText(exchange, e.getMessage(), 406);
            }
        } else {
            try {
                manager.updateSubtask(subtask);
                sendText(exchange, "Подзадача обновлена", 201);
            } catch (NotFoundException e) {
                sendText(exchange, "Подзадачи с id" + id + " не существует", 404);
            } catch (IntersectionException e) {
                sendText(exchange, e.getMessage(), 406);
            }
        }
    }

    @Override
    protected void processGet(HttpExchange exchange, String path, Integer id) throws IOException {
        if (path.split("/").length <= 2) {
            sendText(exchange, manager.getSubtasks(), 200);
            return;
        }
        try {
            sendText(exchange, manager.getSubtaskById(id), 200);
        } catch (NotFoundException e) {
            sendText(exchange, "Задачи с id" + id + " не существует", 404);
        }
    }
}
