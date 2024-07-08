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
import java.util.TreeSet;

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
        Task newTask = taskManager.create(task);
        sortedSet.add(newTask);
        return newTask.getID();
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
        sortedSet.remove(task);
        sortedSet.add(taskManager.update(task));
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
            subtaskManager.deleteByEpic(id);
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
        subtaskManager.delete();
        epicManager.delete();
    }


    @Override
    public ArrayList<Subtask> getSubtasksByEpic(int epicID) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : epicManager.getById(epicID).getSubtasks()) {
            subtasks.add(subtaskManager.getById(subtaskId).copy());
        }
        return subtasks;
    }


    @Override
    public int createSubtask(Subtask subtask) {
        Subtask newSubtask = subtaskManager.create(subtask);
        int epicID = subtask.getEpicID();
        epicManager.getById(epicID).addSubtask(subtask.getID());
        epicManager.updateEpic(epicID, getSubtasksByEpic(epicID));
        sortedSet.add(newSubtask);
        return subtask.getID();
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
        sortedSet.remove(subtask);
        sortedSet.add(subtaskManager.update(subtask));
        int epicID = subtask.getEpicID();
        epicManager.updateEpic(epicID, getSubtasksByEpic(epicID));
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
        for (Task task : list) {
            historyManager.remove(task.getID());
        }
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




}











