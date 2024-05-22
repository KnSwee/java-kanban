import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.controller.InMemoryHistoryManager;
import project.controller.InMemoryTaskManager;
import project.controller.api.HistoryManager;
import project.controller.api.TaskManager;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.util.Managers;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralTest {

    TaskManager manager;
    HistoryManager inMemoryHistoryManager;
    Task baseTask;
    Epic baseEpic;
    Subtask baseSubtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        inMemoryHistoryManager = Managers.getDefaultHistory();
        baseTask = new Task("BaseTask", "BaseDescription");
        manager.createTask(baseTask);
        baseEpic = new Epic("BaseEpic", "BaseDescription");
        manager.createEpic(baseEpic);
        baseSubtask = new Subtask("BaseSubtask", "BaseDescription", baseEpic.getID());
        manager.createSubtask(baseSubtask);
    }

    @AfterEach
    void tearDown() {
        ((InMemoryHistoryManager) inMemoryHistoryManager).clearHistory();
    }

    @Test
    void shouldAddToHistory() {
        int expectedHistorySize = 3;

        manager.getTaskById(baseTask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getSubtaskById(baseSubtask.getID());

        assertEquals(expectedHistorySize, inMemoryHistoryManager.getHistory().size());
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

        assertEquals(expectedHistorySize, inMemoryHistoryManager.getHistory().size());
        assertNotEquals(manager.getTaskById(baseTask.getID()), inMemoryHistoryManager.getHistory().getFirst());
    }

    @Test
    void shouldReturnOldTaskInHistoryAfterUpdating() {
        Task taskById = manager.getTaskById(baseTask.getID());

        manager.updateTask(new Task("NewName", "NewDescr", baseTask.getID()));

        assertEquals(baseTask.toString(), inMemoryHistoryManager.getHistory().getFirst().toString());
    }

    @Test
    void shouldReturnManagerObject() {
        assertNotNull(Managers.getDefault());
    }

}
