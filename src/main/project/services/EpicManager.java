package project.services;

import project.controller.InMemoryTaskManager;
import project.enums.Status;
import project.models.Epic;
import project.models.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicManager implements Manager<Epic> {

    private final HashMap<Integer, Epic> epics = new HashMap<>();


    @Override
    public ArrayList<Epic> get() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(epic.copy());
        }
        return epicList;
    }

    @Override
    public Epic create(Epic epic) {
        epic.setID(InMemoryTaskManager.getID());
        epics.put(epic.getID(), epic);
        return getById(epic.getID());
    }

    @Override
    public void fillMap(Epic task) {
        epics.put(task.getID(), task);
    }

    @Override
    public Epic getById(int id) {
        return epics.get(id);
    }

    @Override
    public void delete() {
        epics.clear();
    }

    @Override
    public void update(Epic epic) {
        if (!(epic.getName() == null)) {
            getById(epic.getID()).setName(epic.getName());
        }
        if (!(epic.getDescription() == null)) {
            getById(epic.getID()).setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteById(int id) {
        epics.remove(id);
    }

    public void updateEpicStatus(int epicID, ArrayList<Subtask> subtasks) {
        int doneCounter = 0;
        int newCounter = 0;
        Epic epic = getById(epicID);
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
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void resetEpics() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

}
