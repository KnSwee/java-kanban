package project.services;

import project.controller.InMemoryTaskManager;
import project.models.Subtask;

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
    public void update(Subtask subtask) {
        if (!(subtask.getName() == null)) {
            getById(subtask.getID()).setName(subtask.getName());
        }
        if (!(subtask.getDescription() == null)) {
            getById(subtask.getID()).setDescription(subtask.getDescription());
        }
        if (!(subtask.getStatus() == null)) {
            getById(subtask.getID()).setStatus(subtask.getStatus());
        }
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
