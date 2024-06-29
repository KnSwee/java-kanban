package project.controller;

import project.enums.Status;
import project.enums.TaskType;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static final String DELIMITER = ";";
    Path path;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public void save() {

    }

    private static String toString(Task task) {
        StringBuilder sb = new StringBuilder(task.getID() + DELIMITER + task.getType() + DELIMITER + task.getName() +
                DELIMITER + task.getStatus() + DELIMITER + task.getDescription());
        if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic)task;
            sb.append(epic.getSubtasks().toString());
        } else if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask)task;
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
                ids.add(Integer.parseInt(s));
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
