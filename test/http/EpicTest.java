package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.HttpTaskServer;
import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.exceptions.NotFoundException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {


    public static final LocalDateTime TIME = LocalDateTime.of(2024, 2, 2, 14, 0);
    TaskManager manager;
    Task baseTask;
    Epic baseEpic;
    Subtask baseSubtask;
    HttpServer server;
    HttpTaskServer taskServer;
    Gson gson;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        HttpTaskServer.startServer(manager);
        manager = new InMemoryTaskManager();
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
    public void shouldGetEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epicsJson = gson.toJson(manager.getEpics());

        assertEquals(200, response.statusCode());
        assertEquals(epicsJson, response.body());
    }

    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + baseEpic.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epicJson = gson.toJson(manager.getEpicById(baseEpic.getID()));

        assertEquals(200, response.statusCode());
        assertEquals(epicJson, response.body());
    }

    @Test
    public void shouldNotGetEpicWhichDoesNotExist() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + 123);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldGetEpicSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + baseEpic.getID() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epicJson = gson.toJson(manager.getSubtasksByEpic(baseEpic.getID()));

        assertEquals(200, response.statusCode());
        assertEquals(epicJson, response.body());
    }

    @Test
    public void shouldNotGetSubtasksByEpicWhichDoesNotExist() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + 3342 + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Name2", "descr", null);

        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(2, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Name2", epicsFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + baseEpic.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, response.statusCode());
        assertThrows(NotFoundException.class, () -> manager.getEpicById(baseEpic.getID()));
    }

}



