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
    public void fillMap(Task task) {
        tasks.put(task.getID(), task);
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
    public Task update(Task task) {
        Task byId = getById(task.getID());
        if (!(task.getName() == null)) {
            byId.setName(task.getName());
        }
        if (!(task.getDescription() == null)) {
            byId.setDescription(task.getDescription());
        }
        if (!(task.getStatus() == null)) {
            byId.setStatus(task.getStatus());
        }
        return byId;
    }

    @Override
    public void deleteById(int id) {
        tasks.remove(id);
    }
}
