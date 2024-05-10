package project;

import project.controller.Controller;
import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

public class Main {

    public static void main(String[] args) {

        Controller controller = new Controller();

        int firstTaskId = controller.createTask(new Task("Task 1", "testTask1"));
        int secondTaskId = controller.createTask(new Task("Task 2", "testTask2"));
        int firstEpicId = controller.createEpic(new Epic("Epic 1", "testEpic"));
        int subtaskId1 = controller.createSubtask(
                new Subtask("Subtask 1 (Epic 1)", "testSubtask1", firstEpicId));
        int subtaskId2 = controller.createSubtask(
                new Subtask("Subtask 2 (Epic 1)", "testSubtask1", firstEpicId));
        int secondEpicId = controller.createEpic(new Epic("Epic 2", "testEpic2"));
        int subtaskId3 = controller.createSubtask(
                new Subtask("Subtask 3 (Epic 2)", "testSubtask3", secondEpicId));

        System.out.println("...tasks created...");
        System.out.println(controller.getTasks());
        System.out.println(controller.getEpics());
        System.out.println(controller.getSubtasks());

        controller.updateTask(new Task(firstTaskId, null, null, "IN_PROGRESS"));
        controller.updateTask(new Task(secondTaskId, null, null, "DONE"));
        controller.updateSubtask(new Subtask(subtaskId1, null, null, "IN_PROGRESS", firstEpicId));
        controller.updateSubtask(new Subtask(subtaskId2, null, null, "DONE", firstEpicId));
        controller.updateSubtask(new Subtask(subtaskId3, null, null, "DONE", secondEpicId));

        System.out.println("...tasks updated...");
        System.out.println(controller.getTasks());
        System.out.println(controller.getEpics());
        System.out.println(controller.getSubtasks());

        controller.deleteTaskById(firstTaskId);
        controller.deleteEpicById(firstEpicId);
        controller.deleteSubtasksByEpic(secondEpicId);

        System.out.println("...tasks deleted...");
        System.out.println(controller.getAll());

        System.out.println("\n" + controller.getById(secondEpicId));

    }
}

