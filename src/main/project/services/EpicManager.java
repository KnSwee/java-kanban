package project.services;

import project.controller.InMemoryTaskManager;
import project.enums.Status;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EpicManager implements Manager<Epic> {

    private final HashMap<Integer, Epic> epics = new HashMap<>();


    @Override
    public List<Epic> get() {
        return epics.values().stream()
                .map(Epic::copy)
                .toList();

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
    public Task update(Epic epic) {
        if (!(epic.getName() == null)) {
            getById(epic.getID()).setName(epic.getName());
        }
        if (!(epic.getDescription() == null)) {
            getById(epic.getID()).setDescription(epic.getDescription());
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        epics.remove(id);
    }

    public void updateEpic(int epicID, List<Subtask> subtasks) {
        AtomicInteger doneStatusCounter = new AtomicInteger();
        AtomicInteger newStatusCounter = new AtomicInteger();
        Epic epic = getById(epicID);

        subtasks.forEach(subtask -> {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (subtask.getStatus() == Status.DONE) {
                doneStatusCounter.getAndIncrement();
            } else if (subtask.getStatus() == Status.NEW) {
                newStatusCounter.getAndIncrement();
            }
        });

        if (newStatusCounter.get() == subtasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneStatusCounter.get() == subtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

        epic.setStartTime(getStartTime(subtasks));

        epic.setDuration(getDuration(subtasks));

        epic.setEndTime(getEndTime(subtasks));
    }

    private static Duration getDuration(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(Task::getDuration)
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
    }

    private static LocalDateTime getStartTime(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(Task::getStartTime)
                .min((subtask1, subtask2) -> {
                    if (subtask1.isBefore(subtask2)) {
                        return -1;
                    } else if (subtask1.isAfter(subtask2)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).orElse(null);
    }

    private static LocalDateTime getEndTime(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(Task::getEndTime)
                .max((subtask1, subtask2) -> {
                    if (subtask1.isBefore(subtask2)) {
                        return -1;
                    } else if (subtask1.isAfter(subtask2)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).orElse(null);
    }


    public void resetEpics() {
        epics.values().forEach(epic -> {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        });
    }

}
