package project.util;

import com.google.gson.Gson;
import project.controller.InMemoryHistoryManager;
import project.controller.InMemoryTaskManager;
import project.controller.api.HistoryManager;
import project.controller.api.TaskManager;
import project.handlers.adapters.DurationAdapter;
import project.handlers.adapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;


public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new Gson().newBuilder().setPrettyPrinting().serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    }

}

