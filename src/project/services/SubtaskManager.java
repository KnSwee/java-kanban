package project.services;

import project.controller.Controller;
import project.models.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class SubtaskManager {

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void createSubtask(Subtask subtask) {
        subtask.setID(Controller.getID());
        subtasks.put(subtask.getID(), subtask);
    }

    public HashMap<Integer, Subtask> getTasks() {
        return subtasks;
    }

    public Subtask getSubtaskById(int ID) {
        return subtasks.get(ID);
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }

    public void updateSubtask(Subtask subtask) {
        if (!(subtask.getName() == null)) {
            getSubtaskById(subtask.getID()).setName(subtask.getName());
        }
        if (!(subtask.getDescription() == null)) {
            getSubtaskById(subtask.getID()).setDescription(subtask.getDescription());
        }
        if (!(subtask.getStatus() == null)) {
            getSubtaskById(subtask.getID()).setStatus(subtask.getStatus());
        }
    }

    public void deleteById(int ID) {
        subtasks.remove(ID);
    }

    public void deleteByEpic(int epicID) {

        for (Subtask subtask : new ArrayList<>(subtasks.values())) {
            if (epicID == subtask.getEpicID()) {
                subtasks.remove(subtask.getID());
            }
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}
