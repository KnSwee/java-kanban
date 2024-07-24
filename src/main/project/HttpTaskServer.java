package project;

import com.sun.net.httpserver.HttpServer;
import project.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final String TASKS = "/tasks";
    public static final String SUBTASKS = "/subtasks";
    public static final String EPICS = "/epics";
    public static final String HISTORY = "/history";
    public static final String PRIORITIZED = "/prioritized";

    public static void startServer(HttpServer httpServer) {
        httpServer.start();
    }


    public static void main(String[] args) {
        HttpServer httpServer;

        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            throw new RuntimeException("Сервер не смог запуститься", e);
        }

        httpServer.createContext(TASKS, new TaskHandler());
        httpServer.createContext(SUBTASKS, new SubtaskHandler());
        httpServer.createContext(EPICS, new EpicHandler());
        httpServer.createContext(HISTORY, new HistoryHandler());
        httpServer.createContext(PRIORITIZED,  new PrioritizedHandler());

        HttpTaskServer.startServer(httpServer);

        System.out.println(httpServer.getAddress());

    }



}
