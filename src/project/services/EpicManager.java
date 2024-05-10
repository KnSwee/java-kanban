package project.services;

import project.controller.Controller;
import project.enums.Status;
import project.models.Epic;
import project.models.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicManager {

    private final HashMap<Integer, Epic> epics = new HashMap<>();


    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic createEpic(Epic epic) {
        epic.setID(Controller.getID());
        epics.put(epic.getID(), epic);
        return getEpicById(epic.getID());
    }

    public Epic getEpicById(int ID) {
        return epics.get(ID);
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void updateEpic(Epic epic) {
        if (!(epic.getName() == null)) {
            getEpicById(epic.getID()).setName(epic.getName());
        }
        if (!(epic.getDescription() == null)) {
            getEpicById(epic.getID()).setDescription(epic.getDescription());
        }
    }

    public void deleteById(int ID) {
        epics.remove(ID);
    }

    public void updateEpicStatus(int epicID, ArrayList<Subtask> subtasks) {
        int doneCounter = 0;
        int newCounter = 0;
        Epic epic = getEpicById(epicID);
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            } else if (subtask.getStatus() == Status.DONE) {
                doneCounter++;
            } else if (subtask.getStatus() == Status.NEW) {
                newCounter++;
            }
        }
        if (newCounter == subtasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneCounter == subtasks.size()) {
            epic.setStatus(Status.DONE);
        }
    }

    public void resetEpics() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

}
