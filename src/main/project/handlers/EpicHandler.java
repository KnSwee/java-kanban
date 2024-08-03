package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import project.controller.api.TaskManager;
import project.exceptions.NotFoundException;
import project.models.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void processDelete(HttpExchange exchange, String path, Integer id) throws IOException {
        manager.deleteEpicById(id);
        sendText(exchange, "Эпик с id " + id + " успешно удален", 200);
    }

    @Override
    protected void processPost(HttpExchange exchange, String path, Integer id) throws IOException {
        String jsonPostString = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
        Epic epic = gson.fromJson(jsonPostString, Epic.class);
        int result = manager.createEpic(epic);
        sendText(exchange, "Эпик создан с id " + result, 201);
    }

    @Override
    protected void processGet(HttpExchange exchange, String path, Integer id) throws IOException {
        String[] pathMassive = (path.split("/"));
        if (id == null) {
            sendText(exchange, manager.getEpics(), 200);
        } else if (pathMassive.length > 3 && pathMassive[3].equals("subtasks")) {
            try {
                sendText(exchange, manager.getSubtasksByEpic(id), 200);
            } catch (NotFoundException e) {
                sendText(exchange, "Эпика с id " + id + " не существует", 404);
            }
        } else if (pathMassive.length == 3) {
            try {
                sendText(exchange, manager.getEpicById(id), 200);
            } catch (NotFoundException e) {
                sendText(exchange, "Эпика с id " + id + " не существует", 404);
            }
        }
    }
}
