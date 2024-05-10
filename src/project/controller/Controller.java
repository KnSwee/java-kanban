package project.controller;

import project.models.Epic;
import project.models.Subtask;
import project.models.Task;
import project.services.EpicManager;
import project.services.SubtaskManager;
import project.services.TaskManager;

import java.util.ArrayList;

public class Controller {
    private static int counter = 0;
    TaskManager taskManager = new TaskManager();
    EpicManager epicManager = new EpicManager();
    SubtaskManager subtaskManager = new SubtaskManager();


    public static int getID() {
        return ++counter;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskManager.getTasks());
    }

    public int createTask(Task task) {
        return taskManager.createTask(task).getID();
    }

    public Task getTaskById(int ID) {
        return taskManager.getTaskById(ID);
    }

    public void deleteTasks() {
        taskManager.deleteTasks();
    }

    public void updateTask(Task task) {
        taskManager.updateTask(task);
    }

    public void deleteTaskById(int ID) {
        taskManager.deleteById(ID);
    }


    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicManager.getEpics());
    }

    public int createEpic(Epic epic) {
        return epicManager.createEpic(epic).getID();
    }

    public Epic getEpicById(int ID) {
        return epicManager.getEpicById(ID);
    }

    public Epic getEpicBySubtask(int subtaskId) {
        return getEpicById(getSubtaskById(subtaskId).getEpicID());
    }

    public void deleteEpicById(int ID) {
        epicManager.deleteById(ID);
        subtaskManager.deleteByEpic(ID);
    }

    public void updateEpic(Epic epic) {
        epicManager.updateEpic(epic);
        epicManager.updateEpicStatus(epic.getID(), getSubtasksByEpic(epic.getID()));
    }

    public void deleteEpics() {
        epicManager.deleteEpics();
        subtaskManager.deleteSubtasks();
    }


    public ArrayList<Subtask> getSubtasksByEpic(int epicID) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : epicManager.getEpicById(epicID).getSubtasks()) {
            subtasks.add(getSubtaskById(subtaskId));
        }
        return subtasks;
    }


    public int createSubtask(Subtask subtask) {
        subtaskManager.createSubtask(subtask);
        int epicID = subtask.getEpicID();
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
        epicManager.getEpicById(epicID).addSubtask(subtask.getID());
        return subtask.getID();
    }


    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskManager.getSubtasks());
    }

    public Subtask getSubtaskById(int ID) {
        return subtaskManager.getSubtaskById(ID);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskManager.updateSubtask(subtask);
        int epicID = subtask.getEpicID();
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    public void deleteSubtasks() {
        subtaskManager.deleteSubtasks();
        epicManager.resetEpics();
    }

    public void deleteSubtaskById(int ID) {
        int epicID = subtaskManager.getSubtaskById(ID).getEpicID();
        subtaskManager.deleteById(ID);
        getEpicById(epicID).removeSubtask(ID);
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    public void deleteSubtasksByEpic(int epicID) {
        for (int subtaskId : new ArrayList<>(getEpicById(epicID).getSubtasks())) {
            deleteSubtaskById(subtaskId);
        }
    }

    public Task getById(int id) {
        Task result;
        result = taskManager.getTaskById(id);
        if (result != null) {
            return result;
        }
        result = epicManager.getEpicById(id);
        if (result != null) {
            return result;
        }
        result = subtaskManager.getSubtaskById(id);
        return result;
    }

    public ArrayList<Task> getAll() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks());
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());
        return allTasks;
    }
}











