import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.exceptions.IntersectionException;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.util.Managers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    public static final LocalDateTime TIME = LocalDateTime.of(2024, 2, 2, 14, 0);
    TaskManager manager;
    Task baseTask;
    Epic baseEpic;
    Subtask baseSubtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        manager.deleteSubtasks();
        baseTask = new Task("BaseTask", "BaseDescription", 10, TIME);
        manager.createTask(baseTask);
        baseEpic = new Epic("BaseEpic", "BaseDescription");
        manager.createEpic(baseEpic);
        baseSubtask = new Subtask("BaseSubtask", "BaseDescription", baseEpic.getID(), 10, TIME.plusMinutes(1000));
        manager.createSubtask(baseSubtask);
    }

    @AfterEach
    void tearDown() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
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
    }

    @Test
    void shouldRemoveTailFromHistory() {
        manager.getSubtaskById(baseSubtask.getID());
        manager.getEpicById(baseEpic.getID());
        manager.getTaskById(baseTask.getID());
        assertEquals(3, manager.getHistory().size());

        manager.deleteTaskById(baseTask.getID());
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
        manager.getTaskById(baseTask.getID());


        manager.updateTask(new Task("NewName", "NewDescr", baseTask.getID(), 1, TIME.plusMinutes(333)));

        assertEquals(expectedTask.toString(), manager.getHistory().getFirst().toString());
    }

    @Test
    void shouldReturnEmptyHistory() {
        assertTrue(manager.getHistory().isEmpty());
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
        assertEquals(taskIdBefore, taskIdAfter);
        assertEquals(subtaskIdBefore, subtaskIdAfter);
        assertEquals(epicIdBefore, epicIdAfter);
    }

    @Test
    void shouldNotCreateTaskWithSameStartTime() {
        int initialSize = manager.getTasks().size();

        assertThrows(IntersectionException.class,
                () -> manager.createTask(new Task("name", "descr", 10, TIME)));

        assertEquals(initialSize, manager.getTasks().size());
    }

    @Test
    void shouldNotCreateTasksThatOverlap() {
        int initialSize = manager.getTasks().size();

        manager.createTask(new Task("name", "descr", 10, TIME.plusYears(1)));

        assertThrows(IntersectionException.class,
                () -> manager.createTask(new Task("name", "descr", 10,
                        TIME.plusYears(1).plusMinutes(5))));

        assertEquals(initialSize + 1, manager.getTasks().size());
    }

    @Test
    void shouldNotCreateTasksThatOverlapWithStartAndEndTime() {
        int initialSize = manager.getTasks().size();

        manager.createTask(new Task("name", "descr", 10, TIME.plusYears(1)));

        assertThrows(IntersectionException.class,
                () -> manager.createTask(new Task("name", "descr", 10,
                        TIME.plusYears(1).plusMinutes(10))));

        assertEquals(initialSize + 1, manager.getTasks().size());
    }

    @Test
    void shouldCreateTasksThatNotOverlap() {
        int initialSize = manager.getTasks().size();

        manager.createTask(new Task("name", "descr", 10, TIME.plusYears(1)));
        manager.createTask(new Task("name", "descr", 10, TIME.plusYears(1).plusMinutes(11)));

        assertEquals(initialSize + 2, manager.getTasks().size());
    }

    @Test
    void shouldReturnPrioritizedTasks() {
        manager.createTask(new Task("1 year from now", "descr", 100, TIME.plusYears(1)));
        int firstId = manager.createTask(new Task("1 year ago from now", "descr", 100, TIME.minusYears(1)));
        int lastId = manager.createTask(new Task("the last", "descr", 100, TIME.plusYears(76)));

        System.out.println(manager.getPrioritizedTasks());
        System.out.println(manager.getSubtasks());
        assertEquals(manager.getTaskById(firstId), manager.getPrioritizedTasks().get(0));
        assertEquals(manager.getTaskById(lastId), manager.getPrioritizedTasks().get(manager.getPrioritizedTasks().size() - 1));
    }

    @Test
    void shouldUpdateTaskThatOverlapThemselve() {
        int taskId = manager.createTask(new Task("name", "descr", 10, TIME.plusYears(111)));

        manager.updateTask(new Task("newName", "realNewDescr", taskId, 10, TIME.plusYears(111)));

        assertEquals("newName", manager.getTaskById(taskId).getName());
    }

}
