package project.services;

import project.controller.Controller;
import project.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();


    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task createTask(Task task) {
        task.setID(Controller.getID());
        tasks.put(task.getID(), task);
        return getTaskById(task.getID());
    }

    public Task getTaskById(int ID) {
        return tasks.get(ID);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void updateTask(Task task) {
        if (!(task.getName() == null)) {
            getTaskById(task.getID()).setName(task.getName());
        }
        if (!(task.getDescription() == null)) {
            getTaskById(task.getID()).setDescription(task.getDescription());
        }
        if (!(task.getStatus() == null)) {
            getTaskById(task.getID()).setStatus(task.getStatus());
        }
    }

    public void deleteById(int ID) {
        tasks.remove(ID);
    }

}
