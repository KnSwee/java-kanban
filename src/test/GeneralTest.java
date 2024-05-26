import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.util.Managers;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralTest {

    TaskManager manager;
    Task baseTask;
    Epic baseEpic;
    Subtask baseSubtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();

        baseTask = new Task("BaseTask", "BaseDescription");
        manager.createTask(baseTask);
        baseEpic = new Epic("BaseEpic", "BaseDescription");
        manager.createEpic(baseEpic);
        baseSubtask = new Subtask("BaseSubtask", "BaseDescription", baseEpic.getID());
        manager.createSubtask(baseSubtask);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldAddToHistory() {
        int expectedHistorySize = 3;

        manager.getTaskById(baseTask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());

        assertEquals(expectedHistorySize, manager.getHistory().size());
    }

    @Test
    void shouldDeleteTenthElementFromHistoryWhenTryAddElevensElement() {
        int expectedHistorySize = 10;

        manager.getTaskById(baseTask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());

        assertEquals(expectedHistorySize, manager.getHistory().size());
        assertNotEquals(manager.getTaskById(baseTask.getID()), manager.getHistory().getFirst());
    }

    @Test
    void shouldReturnOldTaskInHistoryAfterUpdating() {
        Task expectedTask = baseTask.copy();
        Task taskById = manager.getTaskById(baseTask.getID());


        manager.updateTask(new Task("NewName", "NewDescr", baseTask.getID()));

        assertEquals(expectedTask.toString(), manager.getHistory().getFirst().toString());
    }

    @Test
    void shouldReturnManagerObject() {
        assertNotNull(Managers.getDefault());
    }

}
