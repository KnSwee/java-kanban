package project.util;

import project.controller.InMemoryHistoryManager;
import project.controller.InMemoryTaskManager;
import project.controller.api.HistoryManager;
import project.controller.api.TaskManager;


public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

