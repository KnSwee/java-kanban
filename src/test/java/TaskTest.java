import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.enums.Status;
import project.models.Task;
import project.util.Managers;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    project.controller.api.TaskManager inMemoryTaskManager;
    Task baseTask = new Task("BaseTask", "BaseDescription");

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = Managers.getDefault();
        baseTask.setID(inMemoryTaskManager.createTask(new Task(baseTask)));
        inMemoryTaskManager.createTask(new Task(2, "Task2", "Description2", Status.NEW.name()));
    }

    @AfterEach
    void tearDown() {
        inMemoryTaskManager.deleteTasks();

    }

    @Test
    void shouldCreateTaskAndReturnTaskObject() {
        Task expectedTask = new Task("NewTask", "NewDescription");
        int initialCollectionSize = inMemoryTaskManager.getTasks().size();

        int id = inMemoryTaskManager.createTask(expectedTask);

        assertEquals((initialCollectionSize + 1), inMemoryTaskManager.getTasks().size());
        assertEquals(expectedTask, inMemoryTaskManager.getTaskById(id));
    }

    @Test
    void shouldReturnTaskById() {
        assertEquals(baseTask, inMemoryTaskManager.getTaskById(baseTask.getID()));
    }

    @Test
    void shouldClearTasks() {
        int expectedCollectionSize = 0;

        inMemoryTaskManager.deleteTasks();

        assertEquals(expectedCollectionSize, inMemoryTaskManager.getTasks().size());
    }

    @Test
    void shouldUpdateTask() {
        Task expectedTask = new Task(baseTask.getID(), "NewName", "NewDescription", Status.DONE.name());

        inMemoryTaskManager.updateTask(expectedTask);

        assertEquals(expectedTask, inMemoryTaskManager.getTaskById(expectedTask.getID()));
    }

    @Test
    void shouldDeleteTaskById() {
        int initialCollectionSize = inMemoryTaskManager.getTasks().size();

        inMemoryTaskManager.deleteTaskById(baseTask.getID());

        assertNull(inMemoryTaskManager.getTaskById(baseTask.getID()));
        assertEquals(initialCollectionSize - 1, inMemoryTaskManager.getTasks().size());
    }

    @Test
    void shouldReturnListOfTasks() {
        int expectedCollectionSize = 2;

        assertEquals(expectedCollectionSize, inMemoryTaskManager.getTasks().size());
        for (Task task : inMemoryTaskManager.getTasks()) {
            if (baseTask.getID() == task.getID()) {
                assertEquals(baseTask.toString(), task.toString());
            }
        }
    }

    @Test
    void shouldReturnTrueForDifferentTasksWithSameId() {
        Task firstTask = new Task(baseTask.getID(), "Same", "Same", Status.IN_PROGRESS.name());
        Task secondTask = new Task(baseTask.getID(), "notSame", "notSame123", Status.NEW.name());

        assertEquals(firstTask, secondTask);
        assertNotEquals(firstTask.toString(), secondTask.toString());
    }


}