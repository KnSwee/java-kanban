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
        int expectedHistorySize = 0;

        assertEquals(expectedHistorySize++, manager.getHistory().size());
        manager.getTaskById(baseTask.getID());
        assertEquals(expectedHistorySize++, manager.getHistory().size());
        manager.getEpicById(baseEpic.getID());
        assertEquals(expectedHistorySize++, manager.getHistory().size());
        manager.getSubtaskById(baseSubtask.getID());
        assertEquals(expectedHistorySize, manager.getHistory().size());
    }

    @Test
    void shouldReturnLastTaskInHistory() {

        manager.getTaskById(baseTask.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getTaskById(baseTask.getID());
        assertEquals(2, manager.getHistory().size());
        assertEquals(baseTask, manager.getHistory().getLast());
    }

    @Test
    void shouldRemoveOnlyOneTaskFromHistory() {
        manager.getSubtaskById(baseSubtask.getID());
        assertEquals(1, manager.getHistory().size());
        manager.deleteSubtaskById(baseSubtask.getID());
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    void shouldNotRemoveAnotherTaskFromHistory() {
        manager.getTaskById(baseTask.getID());
        assertEquals(1, manager.getHistory().size());
        manager.deleteSubtaskById(baseSubtask.getID());
        assertEquals(1, manager.getHistory().size());
        assertEquals(baseTask, manager.getHistory().getFirst());
    }

    @Test
    void shouldRemoveHeadFromHistory() {
        manager.getTaskById(baseTask.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        assertEquals(3, manager.getHistory().size());

        manager.deleteTaskById(baseTask.getID());
        assertEquals(2, manager.getHistory().size());
        manager.deleteTaskById(baseTask.getID());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void shouldRemoveTailFromHistory() {
        manager.getTaskById(baseTask.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        assertEquals(3, manager.getHistory().size());

        manager.deleteEpicById(baseEpic.getID());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void shouldRemoveMiddleTaskFromHistory() {
        manager.getTaskById(baseTask.getID());
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        assertEquals(3, manager.getHistory().size());

        manager.deleteSubtaskById(baseSubtask.getID());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void shouldDeleteDoubles() {
        int expectedHistorySize = 1;

        manager.getTaskById(baseTask.getID());
        manager.getTaskById(baseTask.getID());
        manager.getTaskById(baseTask.getID());
        manager.getTaskById(baseTask.getID());

        assertEquals(expectedHistorySize, manager.getHistory().size());
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

    @Test
    void shouldNotReturnRealObjectFromCollection() {
        int taskIdBefore = manager.getTasks().getFirst().getID();
        manager.getTasks().getFirst().setID(999);
        int taskIdAfter = manager.getTasks().getFirst().getID();
        int subtaskIdBefore = manager.getSubtasks().getFirst().getID();
        manager.getSubtasks().getFirst().setID(999);
        int subtaskIdAfter = manager.getSubtasks().getFirst().getID();
        int epicIdBefore = manager.getEpics().getFirst().getID();
        manager.getEpics().getFirst().setID(999);
        int epicIdAfter = manager.getEpics().getFirst().getID();
        assertEquals(taskIdBefore,taskIdAfter);
        assertEquals(subtaskIdBefore,subtaskIdAfter);
        assertEquals(epicIdBefore,epicIdAfter);
    }

}
