package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import project.controller.api.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override

    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathMassive = (path.split("/"));
        if (method.equals("GET")) {
            sendText(exchange, manager.getHistory(), 200);
        }
        unaviableMethod(exchange);
    }
}
