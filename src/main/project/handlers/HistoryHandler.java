package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import project.controller.api.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void processGet(HttpExchange exchange, String path, Integer id) throws IOException {
        sendText(exchange, manager.getHistory(), 200);
    }
}
