package project.services;

import project.controller.InMemoryTaskManager;
import project.exceptions.NotFoundException;
import project.models.Subtask;
import project.models.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SubtaskManager implements Manager<Subtask> {

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public List<Subtask> get() {
        return subtasks.values().stream()
                .map(Subtask::copy)
                .toList();
    }

    @Override
    public Subtask create(Subtask subtask) {
        subtask.setID(InMemoryTaskManager.getID());
        subtasks.put(subtask.getID(), subtask);
        return getById(subtask.getID());
    }


    @Override
    public Subtask getById(int id) {
        return Optional.ofNullable(subtasks.get(id)).orElseThrow(NotFoundException::new);
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
        if (!(subtask.getDuration() == null || subtask.getDuration().toSeconds() == 0)) {
            byId.setDuration(subtask.getDuration());
        }
        if (!(subtask.getStartTime() == null)) {
            byId.setStartTime(subtask.getStartTime());
        }
        return byId;
    }

    @Override
    public void deleteById(int id) {
        subtasks.remove(id);
    }

    public void deleteByEpic(int epicID) {
        subtasks.values().forEach(subtask -> {
            if (epicID == subtask.getEpicID()) {
                subtasks.remove(subtask.getID());
            }
        });
    }
}
