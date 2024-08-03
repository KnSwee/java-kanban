import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.exceptions.NotFoundException;
import project.models.Epic;
import project.models.Subtask;
import project.util.Managers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicManagerTest {

    static Epic baseEpic = new Epic("BaseEpic", "BaseDescription");
    project.controller.api.TaskManager inMemoryTaskManager;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = Managers.getDefault();
        baseEpic.setID(inMemoryTaskManager.createEpic(new Epic(baseEpic)));
        inMemoryTaskManager.createEpic(new Epic("Epic2", "Description2", 2));
    }

    @AfterEach
    void tearDown() {
        inMemoryTaskManager.deleteEpics();
    }


    @Test
    void shouldReturnListOfEpics() {
        int expectedCollectionSize = 2;

        assertEquals(expectedCollectionSize, inMemoryTaskManager.getEpics().size());
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            if (baseEpic.getID() == epic.getID()) {
                assertEquals(baseEpic.toString(), epic.toString());
            }
        }
    }

    @Test
    void shouldCreateEpicAndReturnEpicObject() {
        Epic expectedEpic = new Epic("NewEpic", "NewDescription", 111);
        int initialCollectionSize = inMemoryTaskManager.getEpics().size();

        inMemoryTaskManager.createEpic(expectedEpic);

        assertEquals((initialCollectionSize + 1), inMemoryTaskManager.getEpics().size());
        assertEquals(expectedEpic, inMemoryTaskManager.getEpics().getLast());
    }

    @Test
    void shouldReturnEpicById() {
        assertEquals(baseEpic, inMemoryTaskManager.getEpicById(baseEpic.getID()));
    }

    @Test
    void shouldReturnEpicBySubtaskId() {
        int subtaskId = inMemoryTaskManager.createSubtask(new Subtask("Subtask", "Description", baseEpic.getID(), 10, LocalDateTime.now()));

        assertEquals(baseEpic, inMemoryTaskManager.getEpicBySubtask(subtaskId));
    }

    @Test
    void shouldDeleteEpicById() {
        int initialCollectionSize = inMemoryTaskManager.getEpics().size();

        inMemoryTaskManager.deleteEpicById(baseEpic.getID());

        assertThrows(NotFoundException.class, () -> inMemoryTaskManager.getEpicById(baseEpic.getID()));
        assertEquals(initialCollectionSize - 1, inMemoryTaskManager.getEpics().size());
    }

    @Test
    void shouldUpdateEpic() {
        Epic expectedEpic = new Epic("NewName", "NewDescription", baseEpic.getID());

        inMemoryTaskManager.updateEpic(expectedEpic);

        assertEquals(expectedEpic, inMemoryTaskManager.getEpicById(expectedEpic.getID()));
    }

    @Test
    void shouldDeleteEpics() {
        int expectedCollectionSize = 0;

        inMemoryTaskManager.deleteEpics();

        assertEquals(expectedCollectionSize, inMemoryTaskManager.getEpics().size());
    }

    @Test
    void shouldReturnTrueForDifferentTasksWithSameId() {
        Epic firstEpic = new Epic("Same", "Same", baseEpic.getID());
        Epic secondEpic = new Epic("notSame", "notSame123", baseEpic.getID());

        assertEquals(firstEpic, secondEpic);
        assertNotEquals(firstEpic.toString(), secondEpic.toString());
    }

}