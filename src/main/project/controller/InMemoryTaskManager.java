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

import static java.util.Arrays.asList;

public class InMemoryTaskManager implements TaskManager {


    private static int counter = 0;
    HistoryManager historyManager = Managers.getDefaultHistory();
    private final Manager<Task> taskManager = new project.services.TaskManager();
    private final EpicManager epicManager = new EpicManager();
    private final SubtaskManager subtaskManager = new SubtaskManager();
    private final ArrayList<Manager<? extends Task>> managersList =
            new ArrayList<>(asList(taskManager, epicManager, subtaskManager));


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
        return historyManager.add(byId);
    }

    @Override
    public void deleteTasks() {
        taskManager.delete();
    }

    @Override
    public void updateTask(Task task) {
        taskManager.update(task);
    }

    @Override
    public void deleteTaskById(int ID) {
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
        return historyManager.add(epicManager.getById(ID));
    }

    @Override
    public Epic getEpicBySubtask(int subtaskId) {
        return getEpicById(subtaskManager.getById(subtaskId).getEpicID());
    }

    @Override
    public void deleteEpicById(int ID) {
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
        epicManager.delete();
        subtaskManager.delete();
    }


    @Override
    public ArrayList<Subtask> getSubtasksByEpic(int epicID) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : epicManager.getById(epicID).getSubtasks()) {
            subtasks.add(subtaskManager.getById(subtaskId));
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
        return historyManager.add(subtaskManager.getById(ID));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskManager.update(subtask);
        int epicID = subtask.getEpicID();
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    @Override
    public void deleteSubtasks() {
        subtaskManager.delete();
        epicManager.resetEpics();
    }

    @Override
    public void deleteSubtaskById(int ID) {
        int epicID = subtaskManager.getById(ID).getEpicID();
        subtaskManager.deleteById(ID);
        getEpicById(epicID).removeSubtask(ID);
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    @Override
    public void deleteSubtasksByEpic(int epicID) {
        for (int subtaskId : new ArrayList<>(getEpicById(epicID).getSubtasks())) {
            deleteSubtaskById(subtaskId);
        }
    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Возвращает задачи всех типов одним списком
     *
     * @return задачи всех типов
     */
//    public ArrayList<Task> getAll() {
//        ArrayList<Task> allTasks = new ArrayList<>();
//        allTasks.addAll(getTasks());
//        allTasks.addAll(getEpics());
//        allTasks.addAll(getSubtasks());
//        return allTasks;
//    }
//
//    public Task getById(int id) {
//        Task result;
//        result = taskManager.getById(id);
//        if (result != null) {
//            return result;
//        }
//        result = epicManager.getById(id);
//        if (result != null) {
//            return result;
//        }
//        result = subtaskManager.getById(id);
//        return result;
//    }

}











