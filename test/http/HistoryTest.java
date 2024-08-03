package http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.HttpTaskServer;
import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryTest {

    public static final LocalDateTime TIME = LocalDateTime.of(2024, 2, 2, 14, 0);
    TaskManager manager;
    Task baseTask;
    Epic baseEpic;
    Subtask baseSubtask;

    Gson gson;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        HttpTaskServer.startServer(manager);
        baseTask = new Task("BaseTask", "BaseDescription", 10, TIME);
        manager.createTask(baseTask);
        baseEpic = new Epic("BaseEpic", "BaseDescription");
        manager.createEpic(baseEpic);
        baseSubtask = new Subtask("BaseSubtask", "BaseDescription", baseEpic.getID(), 10, TIME.plusMinutes(1000));
        manager.createSubtask(baseSubtask);
        gson = Managers.getGson();
    }

    @AfterEach
    void tearDown() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        HttpTaskServer.stopServer();
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {
        manager.getTaskById(baseTask.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String historyJson = gson.toJson(manager.getHistory());

        assertEquals(200, response.statusCode());
        assertEquals(historyJson, response.body());
    }


}