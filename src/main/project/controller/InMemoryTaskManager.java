package project.controller;

import project.controller.api.HistoryManager;
import project.controller.api.TaskManager;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.services.EpicManager;
import project.services.Manager;
import project.services.SubtaskManager;
import project.util.Managers;

import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {


    protected static int counter = 0;
    protected static final Manager<Task> taskManager = new project.services.TaskManager();
    protected static final EpicManager epicManager = new EpicManager();
    protected static final SubtaskManager subtaskManager = new SubtaskManager();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public static int getID() {
        return ++InMemoryTaskManager.counter;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskManager.get());
    }

    @Override
    public int createTask(Task task) {
        return taskManager.create(task).getID();
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
        deleteFromHistory(getTasks());
        taskManager.delete();
    }

    @Override
    public void updateTask(Task task) {
        taskManager.update(task);
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        taskManager.deleteById(id);
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
        historyManager.remove(id);
        epicManager.deleteById(id);
        subtaskManager.deleteByEpic(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicManager.update(epic);
        epicManager.updateEpicStatus(epic.getID(), getSubtasksByEpic(epic.getID()));
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
        subtaskManager.create(subtask);
        int epicID = subtask.getEpicID();
        epicManager.getById(epicID).addSubtask(subtask.getID());
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
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
        subtaskManager.update(subtask);
        int epicID = subtask.getEpicID();
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    @Override
    public void deleteSubtasks() {
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
            historyManager.remove(id);
            int epicID = subtaskManager.getById(id).getEpicID();
            subtaskManager.deleteById(id);
            epicManager.getById(epicID).removeSubtask(id);
            epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
        }
    }


    @Override
    public void deleteSubtasksByEpic(int epicID) {
        for (int subtaskId : new ArrayList<>(epicManager.getById(epicID).getSubtasks())) {
            deleteSubtaskById(subtaskId);
            historyManager.remove(subtaskId);
        }

    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }


}











