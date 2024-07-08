package project.services;

import project.controller.InMemoryTaskManager;
import project.models.Subtask;
import project.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class SubtaskManager implements Manager<Subtask> {

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public ArrayList<Subtask> get() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask.copy());
        }
        return subtasksList;
    }

    @Override
    public Subtask create(Subtask subtask) {
        subtask.setID(InMemoryTaskManager.getID());
        subtasks.put(subtask.getID(), subtask);
        return getById(subtask.getID());
    }


    @Override
    public Subtask getById(int id) {
        return subtasks.get(id);
    }

    @Override
    public void delete() {
        subtasks.clear();
    }

    @Override
    public void fillMap(Subtask task) {
        subtasks.put(task.getID(), task);
    }

    @Override
    public Task update(Subtask subtask) {
        Subtask byId = getById(subtask.getID());
        if (!(subtask.getName() == null)) {
            byId.setName(subtask.getName());
        }
        if (!(subtask.getDescription() == null)) {
            byId.setDescription(subtask.getDescription());
        }
        if (!(subtask.getStatus() == null)) {
            byId.setStatus(subtask.getStatus());
        }
        return byId;
    }

    @Override
    public void deleteById(int id) {
        subtasks.remove(id);
    }

    public void deleteByEpic(int epicID) {

        for (Subtask subtask : new ArrayList<>(subtasks.values())) {
            if (epicID == subtask.getEpicID()) {
                subtasks.remove(subtask.getID());
            }
        }
    }
}
