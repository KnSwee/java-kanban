package project.controller;

import project.controller.api.HistoryManager;
import project.controller.api.TaskManager;
import project.enums.TaskType;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.services.EpicManager;
import project.services.Manager;
import project.services.SubtaskManager;
import project.util.Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {


    protected static int counter = 0;
    protected static final Manager<Task> taskManager = new project.services.TaskManager();
    protected static final EpicManager epicManager = new EpicManager();
    protected static final SubtaskManager subtaskManager = new SubtaskManager();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected static TreeSet<Task> sortedSet = new TreeSet<>();


    public static int getID() {
        return ++InMemoryTaskManager.counter;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskManager.get());
    }

    @Override
    public int createTask(Task task) {
        if (intersectionChecker(task)) {
            Task newTask = taskManager.create(task);
            addToSortedSet(newTask);
            return newTask.getID();
        }
        return 0;
    }

    protected static void addToSortedSet(Task task) {
        if (task.getStartTime() != null) {
            sortedSet.add(task);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task byId = taskManager.getById(id);
        if (byId == null) {
            return null;
        }
        return historyManager.add(byId.copy());
    }

    @Override
    public void deleteTasks() {
        sortedSet.forEach(task -> {
            if (task.getType() == TaskType.TASK) {
                sortedSet.remove(task);
            }
        });

        deleteFromHistory(getTasks());
        taskManager.delete();
    }

    @Override
    public void updateTask(Task task) {
        if (intersectionChecker(task)) {
            sortedSet.remove(task);
            addToSortedSet(taskManager.update(task));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (taskManager.getById(id) != null) {
            sortedSet.remove(getTaskById(id));
            historyManager.remove(id);
            taskManager.deleteById(id);
        }
    }


    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicManager.get());
    }

    @Override
    public int createEpic(Epic epic) {
        return epicManager.create(epic).getID();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic byId = epicManager.getById(id);
        if (byId == null) {
            return null;
        }
        return historyManager.add(epicManager.getById(id).copy());
    }

    @Override
    public Epic getEpicBySubtask(int subtaskId) {
        return epicManager.getById(subtaskManager.getById(subtaskId).getEpicID()).copy();
    }

    @Override
    public void deleteEpicById(int id) {
        if (epicManager.getById(id) != null) {
            historyManager.remove(id);
            epicManager.deleteById(id);
            deleteSubtasksByEpic(id);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epicManager.update(epic);
        epicManager.updateEpic(epic.getID(), getSubtasksByEpic(epic.getID()));
    }

    @Override
    public void deleteEpics() {
        deleteFromHistory(getSubtasks());
        deleteFromHistory(getEpics());
        deleteSubtasks();
        epicManager.delete();
    }


    @Override
    public List<Subtask> getSubtasksByEpic(int epicID) {
        return epicManager.getById(epicID).getSubtasks()
                .stream()
                .map(subtask -> subtaskManager.getById(subtask).copy())
                .collect(Collectors.toList());
    }


    @Override
    public int createSubtask(Subtask subtask) {
        if (intersectionChecker(subtask)) {
            Subtask newSubtask = subtaskManager.create(subtask);
            int epicID = subtask.getEpicID();
            epicManager.getById(epicID).addSubtask(subtask.getID());
            epicManager.updateEpic(epicID, getSubtasksByEpic(epicID));
            addToSortedSet(newSubtask);
            return subtask.getID();
        }
        return 0;
    }


    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskManager.get());
    }


    @Override
    public Subtask getSubtaskById(int id) {
        Subtask byId = subtaskManager.getById(id);
        if (byId == null) {
            return null;
        }
        return historyManager.add(subtaskManager.getById(id).copy());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (intersectionChecker(subtask)) {
            sortedSet.remove(subtask);
            addToSortedSet(subtaskManager.update(subtask));
            int epicID = subtask.getEpicID();
            epicManager.updateEpic(epicID, getSubtasksByEpic(epicID));
        }
    }

    @Override
    public void deleteSubtasks() {
        sortedSet.forEach(subtask -> {
            if (subtask.getType() == TaskType.SUBTASK) {
                sortedSet.remove(subtask);
            }
        });
        deleteFromHistory(getSubtasks());
        subtaskManager.delete();
        epicManager.resetEpics();

    }

    private void deleteFromHistory(ArrayList<? extends Task> list) {
        list.forEach(task -> historyManager.remove(task.getID()));
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtaskManager.getById(id) != null) {
            sortedSet.remove(getSubtaskById(id));
            historyManager.remove(id);
            int epicID = subtaskManager.getById(id).getEpicID();
            subtaskManager.deleteById(id);
            epicManager.getById(epicID).removeSubtask(id);
            epicManager.updateEpic(epicID, getSubtasksByEpic(epicID));
        }
    }


    @Override
    public void deleteSubtasksByEpic(int epicID) {
        epicManager.getById(epicID)
                .getSubtasks()
                .forEach(subtaskId -> {
                    sortedSet.remove(getSubtaskById(subtaskId));
                    deleteSubtaskById(subtaskId);
                    historyManager.remove(subtaskId);
                });
    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedSet;
    }


    protected static boolean intersectionChecker(Task checkedTask) {
        return sortedSet.stream()
                .filter(task -> !checkedTask.getStartTime().isAfter(task.getEndTime()) && !checkedTask.getEndTime().isBefore(task.getStartTime()))
                .toList().isEmpty();
    }

}







