package project;

import project.controller.InMemoryTaskManager;
import project.controller.api.TaskManager;
import project.models.Task;
import project.util.Managers;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        manager.createTask(new Task("name1", "descr"));
        manager.createTask(new Task("name2", "descr"));
        manager.createTask(new Task("name3", "descr"));
        System.out.println(manager.getTaskById(2));
        manager.updateTask(new Task("name2", "descr", 2));
        System.out.println(manager.getTaskById(2));

    }
}

