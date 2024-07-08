package project;

import project.controller.InMemoryTaskManager;
import project.models.Task;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.createTask(new Task("name2", "descr2", 10, LocalDateTime.now().plusMinutes(120)));
        manager.createTask(new Task("name", "descr", 10, LocalDateTime.now().plusMinutes(100)));


        System.out.println(manager.getPrioritizedTasks());

    }


}



