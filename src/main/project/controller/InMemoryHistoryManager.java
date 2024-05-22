package project.controller;

import project.controller.api.HistoryManager;
import project.models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static final ArrayList<Task> history = new ArrayList<>();

    @Override
    public <T extends Task> T add(T task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.add(task.copy());
        return task;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

    public void clearHistory() {
        history.clear();
    }

}
