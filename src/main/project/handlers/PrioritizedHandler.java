package project.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathMassive = (path.split("/"));
        if (method.equals("GET")) {
            sendText(exchange, manager.getPrioritizedTasks(), 200);
        }
        unaviableMethod(exchange);
    }
}
