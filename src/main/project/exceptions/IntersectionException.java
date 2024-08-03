package project.exceptions;

import project.models.Task;

import java.time.LocalDateTime;
import java.util.List;

import static project.controller.InMemoryTaskManager.FORMATTER;

public class IntersectionException extends RuntimeException {
    public IntersectionException(Task task, List<Task> overlapedTasks) {
        super(saveErrorLog(task, overlapedTasks));
    }

    private static String saveErrorLog(Task task, List<Task> overlapedTasks) {
        String log = LocalDateTime.now().format(FORMATTER) +
                "Задача не может быть добавлена в связи с пересечением времени исполнения\n Проверяемая задача " +
                task + "\n Задачи, с которыми она пересекается " + overlapedTasks;
        System.out.print(log);

        /*String log = String.format("%s: %s %n Проверяемая задача %s%n Задачи, с которыми она пересекается %s%n",
                LocalDateTime.now().format(FORMATTER),
                "Задача не может быть добавлена в связи с пересечением времени исполнения", task, overlapedTasks);*/


        return log;
    }
}
