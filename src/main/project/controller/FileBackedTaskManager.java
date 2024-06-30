package project.controller;

import project.ManagerSaveException;
import project.enums.Status;
import project.enums.TaskType;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static final String DELIMITER = ";";
    Path path;
    File file;


    public FileBackedTaskManager(Path path) {
        this.path = path;
        file = new File(path.toString());
        if (!file.exists()) {
            create(path);
        } else {
           loadFromFile(file);
        }
        try {
            if (file.isDirectory()) {
                throw new ManagerSaveException("Попытка загрузка директории. Ожидался файл.");
            } else if (!file.exists()) {
                throw new ManagerSaveException("Попытка загрузки из несуществующего файла");
            }
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void refreshFile() throws IOException {
        Files.deleteIfExists(path);
        create(path);
    }


    public void loadFromFile(File file) {

        try (FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);
                if (task.getType() == TaskType.TASK) {
                    taskManager.fillMap(task);
                } else if (task.getType() == TaskType.EPIC){
                    epicManager.fillMap((Epic)task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    subtaskManager.fillMap((Subtask)task);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void save() throws IOException {
        refreshFile();
        try (FileWriter fr = new FileWriter(file)) {
            fr.write("id;type;name;status;description;epicId/subtasksId" + System.lineSeparator());
            for (Task task : super.getTasks()) {
                String taskString = toString(task);
                fr.append(taskString);
                fr.append(System.lineSeparator());
            }
            for (Epic epic : super.getEpics()) {
                String epicString = toString(epic);
                fr.append(epicString);
                fr.append(System.lineSeparator());
            }
            for (Subtask subtask : super.getSubtasks()) {
                String subtaskString = toString(subtask);
                fr.append(subtaskString);
                fr.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toString(Task task) {
        StringBuilder sb = new StringBuilder(task.getID() + DELIMITER + task.getType() + DELIMITER + task.getName() +
                DELIMITER + task.getStatus() + DELIMITER + task.getDescription());
        if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic)task;
            sb.append(DELIMITER);
            sb.append(epic.getSubtasks().toString());
        } else if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask)task;
            sb.append(DELIMITER);
            sb.append(subtask.getEpicID());
        }
        return sb.toString();
    }

    private static Task fromString(String task) {
        String[] taskArray = task.split(DELIMITER);
        int id = Integer.parseInt(taskArray[0]);
        Task newTask = new Task(id, taskArray[2], taskArray[4], taskArray[3]);

        if (Objects.equals(taskArray[1], TaskType.TASK.name())) {
            return newTask;
        } else if (Objects.equals(taskArray[1], TaskType.EPIC.name())) {
            String[] subtasksId = taskArray[5].substring(1, taskArray[5].length() - 1).split(",");
            ArrayList<Integer> ids = new ArrayList<>();
            for (String s : subtasksId) {
                ids.add(Integer.parseInt(s.trim()));
            }
            Epic epic = new Epic(taskArray[2], taskArray[4], id);
            epic.setStatus(Status.valueOf(taskArray[3]));
            epic.setSubtasks(ids);
            return epic;
        } else {
            int epicId = Integer.parseInt(taskArray[5]);
            return new Subtask(id, taskArray[2], taskArray[4], taskArray[3], epicId);
        }
    }

}
