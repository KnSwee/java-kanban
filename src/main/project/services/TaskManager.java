package project.services;

import project.controller.InMemoryTaskManager;
import project.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager implements Manager<Task> {

    private final HashMap<Integer, Task> tasks = new HashMap<>();


    @Override
    public ArrayList<Task> get() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task.copy());
        }
        return tasksList;
    }

    @Override
    public Task create(Task task) {
        task.setID(InMemoryTaskManager.getID());
        tasks.put(task.getID(), task);
        return getById(task.getID());
    }

    @Override
    public Task getById(int id) {
        return tasks.get(id);
    }

    @Override
    public void delete() {
        tasks.clear();
    }

    @Override
    public void update(Task task) {
        if (!(task.getName() == null)) {
            getById(task.getID()).setName(task.getName());
        }
        if (!(task.getDescription() == null)) {
            getById(task.getID()).setDescription(task.getDescription());
        }
        if (!(task.getStatus() == null)) {
            getById(task.getID()).setStatus(task.getStatus());
        }
    }

    @Override
    public void deleteById(int id) {
        tasks.remove(id);
    }
}
