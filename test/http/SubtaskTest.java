package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.HttpTaskServer;
import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.enums.Status;
import project.exceptions.NotFoundException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

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
    public void shouldGetSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtasksJson = gson.toJson(manager.getSubtasks());

        assertEquals(200, response.statusCode());
        assertEquals(subtasksJson, response.body());
    }

    @Test
    public void shouldGetSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + baseSubtask.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtaskJson = gson.toJson(manager.getSubtaskById(baseSubtask.getID()));

        assertEquals(200, response.statusCode());
        assertEquals(subtaskJson, response.body());

    }

    @Test
    public void shouldGetErrorSubtaskNotExist() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + 123122312);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldAddSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask(null, "Name2", "DescrQQ",
                Status.NEW.name(), baseEpic.getID(), 5, TIME.plusYears(34));

        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Name2", subtasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldntAddOverlappedSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask(null, "Name2", "Descr",
                Status.NEW.name(), baseEpic.getID(), 5, TIME);

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask(baseSubtask.getID(), "NewName2", "NewDescr",
                Status.NEW.name(), baseEpic.getID(), 5, TIME.minusMonths(31));

        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("NewName2", manager.getSubtaskById(baseSubtask.getID()).getName());
        manager.deleteSubtaskById(subtask.getID());
    }

    @Test
    public void shouldntUpdateOverlappedSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask(baseTask.getID(), "NewName", "NewDescr",
                Status.NEW.name(), baseEpic.getID(), 15, TIME.plusMinutes(1000));

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + baseSubtask.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertThrows(NotFoundException.class, () -> manager.getSubtaskById(baseSubtask.getID()));
    }
}