package project;

import project.controller.FileBackedTaskManager;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {


        FileBackedTaskManager manager = new FileBackedTaskManager(Path.of("./out/file.csv"));



//        manager.createTask(new Task("Z", "Za nashih"));
//        manager.createTask(new Task("u u ue", "ya skuchau po tebe"));
//        manager.createEpic(new Epic("epic3", "sheesh"));
//        manager.createSubtask(new Subtask("subtask1", "wow", 3));
//        manager.createSubtask(new Subtask("subtask2", "wooow", 3));
//
//        manager.save();
//
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());


    }


}



