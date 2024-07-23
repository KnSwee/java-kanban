package project.controller;

import project.enums.Status;
import project.enums.TaskType;
import project.exceptions.ManagerSaveException;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static final String DELIMITER = ";";
    private File file;
    private Path path;

    public FileBackedTaskManager(File file) {
        this.file = file;
        path = file.toPath();
        if (!file.exists()) {
            create(path);
        } else {
            loadFromFile(file);
        }
        if (file.isDirectory()) {
            throw new ManagerSaveException("Попытка загрузка директории. Ожидался файл.");
        } else if (!file.exists()) {
            throw new ManagerSaveException("Попытка загрузки из несуществующего файла");
        }
    }

    private void create(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании файла", e);
        }
    }

    private void refreshFile() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при удалении файла", e);
        }
        create(path);
    }


    public static void loadFromFile(File file) {

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            if (br.ready()) {
                br.readLine();
                counter = Integer.parseInt(br.readLine());
            }
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);
                if (task.getType() == TaskType.TASK) {
                    taskManager.fillMap(task);
                    sortedSet.add(task);
                } else if (task.getType() == TaskType.EPIC) {
                    epicManager.fillMap((Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    subtaskManager.fillMap((Subtask) task);
                    sortedSet.add(task);
                }

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Попытка загрузки из несуществующего файла");
        }

    }

    public void save() {
        refreshFile();
        try (FileWriter fr = new FileWriter(file)) {
            fr.write("id;type;name;status;description;epicId/subtasksId" + System.lineSeparator());
            fr.append(String.valueOf(counter)).append(System.lineSeparator());
            for (Task task : super.getTasks()) {
                fr.append(toString(task)).append(System.lineSeparator());
            }
            for (Epic epic : super.getEpics()) {
                fr.append(toString(epic)).append(System.lineSeparator());
            }
            for (Subtask subtask : super.getSubtasks()) {
                fr.append(toString(subtask)).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден");
        }
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder(task.getID() + DELIMITER + task.getType() + DELIMITER + task.getName() +
                DELIMITER + task.getStatus() + DELIMITER + task.getDescription() + DELIMITER + task.getDuration().toMinutes() +
                DELIMITER + task.getStartTime());
        if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            sb.append(DELIMITER);
            sb.append(epic.getSubtasks().toString());
        } else if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(DELIMITER);
            sb.append(subtask.getEpicID());
        }
        return sb.toString();
    }

    public static Task fromString(String task) {
        String[] taskArray = task.split(DELIMITER);
        int id = Integer.parseInt(taskArray[0]);
        int duration = Integer.parseInt(taskArray[5]);
        LocalDateTime startTime = LocalDateTime.parse(taskArray[6]);

        if (Objects.equals(taskArray[1], TaskType.TASK.name())) {
            return new Task(id, taskArray[2], taskArray[4], taskArray[3], duration, startTime);
        } else if (Objects.equals(taskArray[1], TaskType.EPIC.name())) {
            String[] subtasksId = taskArray[7].substring(1, taskArray[7].length() - 1).split(",");
            List<Integer> ids = Arrays.stream(subtasksId)
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            Epic epic = new Epic(taskArray[2], taskArray[4], id);
            epic.setStartTime(LocalDateTime.parse(taskArray[6]));
            epic.setDuration(Duration.ofMinutes(Long.parseLong(taskArray[5])));
            epic.setStatus(Status.valueOf(taskArray[3]));
            epic.setSubtasks(new ArrayList<>(ids));
            return epic;
        } else {
            int epicId = Integer.parseInt(taskArray[7]);
            return new Subtask(id, taskArray[2], taskArray[4], taskArray[3], epicId, duration, startTime);
        }
    }

    @Override
    public int createTask(Task task) {
        int newTaskId = super.createTask(task);
        save();
        return newTaskId;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public int createEpic(Epic epic) {
        int newEpicId = super.createEpic(epic);
        save();
        return newEpicId;
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int newSubtaskId = super.createSubtask(subtask);
        save();
        return newSubtaskId;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteSubtasksByEpic(int epicID) {
        super.deleteSubtasksByEpic(epicID);
        save();
    }
}
