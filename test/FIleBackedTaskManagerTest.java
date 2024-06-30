import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.controller.FileBackedTaskManager;
import project.controller.api.TaskManager;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FIleBackedTaskManagerTest {

    TaskManager manager;
    Task baseTask;
    Task baseTask2;
    Epic baseEpic;
    Subtask baseSubtask;
    Subtask baseSubtask2;
    Path path = Path.of("./out/testFile.csv");
    File file;


    @BeforeEach
    void setUp() {
        file = new File(String.valueOf(path));
        manager = new FileBackedTaskManager(file);
        baseTask = new Task("BaseTask", "BaseDescription");
        manager.createTask(baseTask);
        baseTask2 = new Task("BaseTask2", "BaseDescription2");
        manager.createTask(baseTask2);
        baseEpic = new Epic("BaseEpic", "BaseDescription");
        manager.createEpic(baseEpic);
        baseSubtask = new Subtask("BaseSubtask", "BaseDescription", baseEpic.getID());
        manager.createSubtask(baseSubtask);
        baseSubtask2 = new Subtask("BaseSubtask2", "BaseDescription2", baseEpic.getID());
        manager.createSubtask(baseSubtask2);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(path);
        Files.deleteIfExists(Path.of("./out/emptyFileTest.csv"));
    }

    @Test
    void shouldSaveFile() {
        assertTrue(Files.exists(path));
    }

    @Test
    void shouldSaveEmptyFile() {
        manager.deleteTasks();
        manager.deleteEpics();
        int stringCount = 0;
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                br.readLine();
                stringCount++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        assertTrue(Files.exists(path));
        assertEquals(2, stringCount);
    }

    @Test
    void shouldLoadFile() {

        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);

        assertEquals(manager.getTasks(), fbtm.getTasks());
        assertEquals(manager.getEpics(), fbtm.getEpics());
        assertEquals(manager.getSubtasks(), fbtm.getSubtasks());

        int fbtmCounter = fbtm.createTask(new Task("name", "descr"));

        assertEquals(baseSubtask2.getID() + 1, fbtmCounter);
    }

    @Test
    void shouldLoadEmptyFile() {
        File emptyFile = new File("./out/emptyFileTest.csv");
        FileBackedTaskManager fbtm = new FileBackedTaskManager(emptyFile);
    }

    @Test
    void shouldCorrectlySerializedAndDeserializedObject() {
        String baseTaskString = FileBackedTaskManager.toString(baseTask);
        Task baseTaskFromString = FileBackedTaskManager.fromString(baseTaskString);

        assertEquals(baseTask.toString(), baseTaskFromString.toString());
    }


}
