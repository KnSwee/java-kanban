import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.enums.Status;
import project.models.Epic;
import project.models.Subtask;
import project.util.Managers;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    project.controller.api.TaskManager inMemoryTaskManager;

    Epic baseEpic = new Epic("BaseEpic", "BaseDescription");
    Subtask baseSubtask1;
    Subtask baseSubtask2;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = Managers.getDefault();
        baseEpic.setID(inMemoryTaskManager.createEpic(new Epic(baseEpic)));
        baseSubtask1 = new Subtask("BaseSubtask1", "BaseDescription", baseEpic.getID());
        baseSubtask2 = new Subtask("BaseSubtask2", "BaseDescription", baseEpic.getID());
        baseSubtask1.setID(inMemoryTaskManager.createSubtask(new Subtask(baseSubtask1)));
        baseSubtask2.setID(inMemoryTaskManager.createSubtask(new Subtask(baseSubtask2)));
    }

    @AfterEach
    void tearDown() {
        inMemoryTaskManager.deleteEpics();
    }

    @Test
    void shouldCreateSubtaskAndReturnSubtaskObject() {
        Subtask expectedSubtask = new Subtask("NewSubtask", "NewDescription", baseEpic.getID());
        int initialCollectionSize = inMemoryTaskManager.getSubtasksByEpic(baseEpic.getID()).size();

        inMemoryTaskManager.createSubtask(expectedSubtask);

        assertEquals((initialCollectionSize + 1), inMemoryTaskManager.getSubtasksByEpic(baseEpic.getID()).size());
        assertEquals(expectedSubtask, inMemoryTaskManager.getSubtasksByEpic(baseEpic.getID()).getLast());
    }

    @Test
    void shouldReturnListOfAllSubtasks() {
        int newEpicId = inMemoryTaskManager.createEpic(new Epic("NewEpic", "NewDescription"));
        inMemoryTaskManager.createSubtask(new Subtask("NewSubtask", "NewDescription", newEpicId));
        int initialCollectionSize = 3;

        assertEquals(initialCollectionSize, inMemoryTaskManager.getSubtasks().size());
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            if (baseSubtask1.getID() == subtask.getID()) {
                assertEquals(baseSubtask1.toString(), subtask.toString());
            }
        }
    }

    @Test
    void shouldReturnSubtaskById() {
        Subtask expectedSubtask = baseSubtask1;

        assertEquals(expectedSubtask, inMemoryTaskManager.getSubtaskById(baseSubtask1.getID()));
    }

    //    @Test
    void shouldUpdateSubtaskWithNullValue() {
        Subtask editSubtask = new Subtask(baseSubtask1.getID(), null, null,
                Status.IN_PROGRESS.name(), baseSubtask1.getEpicID());

        inMemoryTaskManager.updateSubtask(editSubtask);
        Subtask updatedSubtask = inMemoryTaskManager.getSubtaskById(baseSubtask1.getID());

        assertEquals(baseSubtask1.getID(), updatedSubtask.getID());
        assertEquals(editSubtask.getStatus(), updatedSubtask.getStatus());
        assertEquals(baseSubtask1.getName(), updatedSubtask.getName());
        assertEquals(baseSubtask1.getDescription(), updatedSubtask.getDescription());
        assertEquals(baseSubtask1.getEpicID(), updatedSubtask.getEpicID());
    }

    @Test
    void shouldUpdateSubtaskAndUpdateEpicStatusInProgress() {
        Subtask expectedSubtask = new Subtask(baseSubtask1.getID(), "NewSubtask", "NewDescription",
                Status.IN_PROGRESS.name(), baseSubtask1.getEpicID());

        inMemoryTaskManager.updateSubtask(expectedSubtask);

        assertEquals(expectedSubtask, baseSubtask1);
        assertEquals(expectedSubtask.toString(), inMemoryTaskManager.getSubtaskById(baseSubtask1.getID()).toString());
        assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()).getStatus());
    }

    @Test
    void shouldUpdateSubtaskAndUpdateEpicStatusDone() {
        Subtask newStatusSubtask1 = new Subtask(baseSubtask1.getID(), null, null,
                Status.DONE.name(), baseSubtask1.getEpicID());
        Subtask newStatusSubtask2 = new Subtask(baseSubtask2.getID(), null, null,
                Status.DONE.name(), baseSubtask1.getEpicID());

        inMemoryTaskManager.updateSubtask(newStatusSubtask1);
        inMemoryTaskManager.updateSubtask(newStatusSubtask2);

        assertEquals(Status.DONE, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()).getStatus());
    }

    @Test
    void shouldUpdateSubtaskAndUpdateEpicStatusInProgressWithNewAndDone() {
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask1.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask2.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));

        assertEquals(Status.DONE, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()).getStatus());

        inMemoryTaskManager.createSubtask(new Subtask("new", "new", baseEpic.getID()));

        assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()).getStatus());
    }

    @Test
    void shouldUpdateSubtaskAndUpdateEpicStatusToNew() {
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask1.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask2.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));

        assertEquals(Status.DONE, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()).getStatus());

        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask1.getID(), null, null,
                Status.NEW.name(), baseEpic.getID()));
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask2.getID(), null, null,
                Status.NEW.name(), baseEpic.getID()));

        assertEquals(Status.NEW, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()).getStatus());
    }

    @Test
    void shouldDeleteAllSubtasks() {
        int expectedSubtasksSize = 0;

        inMemoryTaskManager.deleteSubtasks();

        assertEquals(expectedSubtasksSize, inMemoryTaskManager.getSubtasks().size());
        assertNull(inMemoryTaskManager.getSubtaskById(baseSubtask1.getID()));
        assertTrue(inMemoryTaskManager.getSubtasks().isEmpty());
    }

    @Test
    void shouldDeleteSubtaskById() {
        int subtaskId = baseSubtask1.getID();

        inMemoryTaskManager.deleteSubtaskById(subtaskId);

        assertNull(inMemoryTaskManager.getSubtaskById(subtaskId));
    }

    @Test
    void shouldDeleteSubtasksByEpicId() {
        inMemoryTaskManager.deleteSubtasksByEpic(baseEpic.getID());

        assertNull(inMemoryTaskManager.getSubtaskById(baseSubtask1.getID()));
        assertNull(inMemoryTaskManager.getSubtaskById(baseSubtask2.getID()));
    }

    @Test
    void shouldReturnEpicBySubtask() {
        assertEquals(baseEpic, inMemoryTaskManager.getEpicBySubtask(baseSubtask1.getID()));
    }

    @Test
    void shouldUpdateEpicStatusWhenDeleteSubtasksByEpic() {
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask1.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask2.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));

        assertEquals(Status.DONE, inMemoryTaskManager.getEpicById(baseEpic.getID()).getStatus());

        inMemoryTaskManager.deleteSubtasksByEpic(baseEpic.getID());

        assertEquals(Status.NEW, inMemoryTaskManager.getEpicById(baseEpic.getID()).getStatus());
    }

    @Test
    void shouldUpdateEpicStatusWhenDeleteAllSubtasks() {
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask1.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));
        inMemoryTaskManager.updateSubtask(new Subtask(baseSubtask2.getID(), null, null,
                Status.DONE.name(), baseEpic.getID()));

        assertEquals(Status.DONE, inMemoryTaskManager.getEpicById(baseEpic.getID()).getStatus());

        inMemoryTaskManager.deleteSubtasks();

        assertEquals(Status.NEW, inMemoryTaskManager.getEpicById(baseEpic.getID()).getStatus());
    }

    @Test
    void shouldReturnTrueForDifferentSubtasksWithSameId() {
        Subtask firstSubtask = new Subtask(baseSubtask1.getID(), "Same", "Same",
                Status.IN_PROGRESS.name(), baseEpic.getID());
        Subtask secondSubtask = new Subtask(baseSubtask1.getID(), "notSame", "notSame123",
                Status.NEW.name(), baseEpic.getID());

        assertEquals(firstSubtask, secondSubtask);
        assertNotEquals(firstSubtask.toString(), secondSubtask.toString());
    }

}