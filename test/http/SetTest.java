package http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.HttpTaskServer;
import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.handlers.adapters.DurationAdapter;
import project.handlers.adapters.LocalDateTimeAdapter;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetTest {

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
        gson = new Gson().newBuilder().setPrettyPrinting().serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    }

    @AfterEach
    void tearDown() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        HttpTaskServer.stopServer();
    }

    @Test
    public void shouldGetPrioritizedSet() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String setJson = gson.toJson(manager.getPrioritizedTasks());

        assertEquals(200, response.statusCode());
        assertEquals(setJson, response.body());
    }


}