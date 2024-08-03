package http;

import com.google.gson.Gson;
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

public class TaskTest {

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
    public void shouldGetTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String tasksJson = gson.toJson(manager.getTasks());

        assertEquals(200, response.statusCode());
        assertEquals(tasksJson, response.body());

    }

    @Test
    public void shouldGetTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + baseTask.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String taskJson = gson.toJson(manager.getTaskById(baseTask.getID()));

        assertEquals(200, response.statusCode());
        assertEquals(taskJson, response.body());
    }

    @Test
    public void shouldGetErrorTaskNotExist() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + 123122312);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        Task task = new Task(null, "Name2", "Descr",
                Status.NEW.name(), 5, TIME.plusYears(123));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Name2", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldntAddOverlappedTask() throws IOException, InterruptedException {
        Task task = new Task(null, "Name2", "Descr",
                Status.NEW.name(), 5, TIME);

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }


    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(baseTask.getID(), "NewName", "NewDescr",
                Status.NEW.name(), 15, TIME.minusMonths(1));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("NewName", manager.getTaskById(baseTask.getID()).getName());
    }

    @Test
    public void shouldntUpdateOverlappedTask() throws IOException, InterruptedException {
        Task task = new Task(baseTask.getID(), "NewName", "NewDescr",
                Status.NEW.name(), 15, TIME.plusMinutes(1000));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }


    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + baseTask.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, response.statusCode());
        assertThrows(NotFoundException.class, () -> manager.getTaskById(baseTask.getID()));
    }
}
