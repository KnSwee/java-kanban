package project;

import com.sun.net.httpserver.HttpServer;
import project.controller.FileBackedTaskManager;
import project.controller.api.TaskManager;
import project.handlers.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class HttpTaskServer {
    public static final String TASKS = "/tasks";
    public static final String SUBTASKS = "/subtasks";
    public static final String EPICS = "/epics";
    public static final String HISTORY = "/history";
    public static final String PRIORITIZED = "/prioritized";
    private static TaskManager manager;
    private static HttpServer httpServer;


    public HttpTaskServer(TaskManager manager) {
        HttpTaskServer.manager = manager;
    }


    public static void startServer(TaskManager manager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            throw new RuntimeException("Сервер не смог запуститься", e);
        }

        httpServer.createContext(TASKS, new TaskHandler(manager));
        httpServer.createContext(SUBTASKS, new SubtaskHandler(manager));
        httpServer.createContext(EPICS, new EpicHandler(manager));
        httpServer.createContext(HISTORY, new HistoryHandler(manager));
        httpServer.createContext(PRIORITIZED, new PrioritizedHandler(manager));
        httpServer.start();
        System.out.println("Сервер запущен по адресу " + httpServer.getAddress());
    }

    public static void stopServer() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен.");
    }

    public static void main(String[] args) {
        Path path = Path.of("./out/Storage.csv");
        File file = new File(String.valueOf(path));
        TaskManager manager = new FileBackedTaskManager(file);

        HttpTaskServer.startServer(manager);

    }


}
