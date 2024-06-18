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


    private static int counter = 0;
    private final Manager<Task> taskManager = new project.services.TaskManager();
    private final EpicManager epicManager = new EpicManager();
    private final SubtaskManager subtaskManager = new SubtaskManager();
    private HistoryManager historyManager = Managers.getDefaultHistory();

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
    public Task getTaskById(int ID) {
        Task byId = taskManager.getById(ID);
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
    public void deleteTaskById(int ID) {
        historyManager.remove(ID);
        taskManager.deleteById(ID);
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
    public Epic getEpicById(int ID) {
        Epic byId = epicManager.getById(ID);
        if (byId == null) {
            return null;
        }
        return historyManager.add(epicManager.getById(ID).copy());
    }

    @Override
    public Epic getEpicBySubtask(int subtaskId) {
        return epicManager.getById(subtaskManager.getById(subtaskId).getEpicID()).copy();
    }

    @Override
    public void deleteEpicById(int ID) {
        historyManager.remove(ID);
        epicManager.deleteById(ID);
        subtaskManager.deleteByEpic(ID);
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
    public Subtask getSubtaskById(int ID) {
        Subtask byId = subtaskManager.getById(ID);
        if (byId == null) {
            return null;
        }
        return historyManager.add(subtaskManager.getById(ID).copy());
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
    public void deleteSubtaskById(int ID) {
        if (subtaskManager.getById(ID) != null) {
            historyManager.remove(ID);
            int epicID = subtaskManager.getById(ID).getEpicID();
            subtaskManager.deleteById(ID);
            epicManager.getById(epicID).removeSubtask(ID);
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











