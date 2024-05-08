public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        int firstTaskId = manager.createTask("Task 1", "testTask1");
        int secondTaskId = manager.createTask("Task 2", "testTask2");
        int firstEpicId = manager.createEpic("Epic 1", "testEpic");
        int subtaskId1 = manager.createSubtask("Subtask 1 (Epic 1)", "testSubtask1", firstEpicId);
        int subtaskId2 = manager.createSubtask("Subtask 2 (Epic 1)", "testSubtask1", firstEpicId);
        int secondEpicId = manager.createEpic("Epic 2", "testEpic2");
        int subtaskId3 = manager.createSubtask("Subtsk 3 (Epic 2)", "testSubtask3", secondEpicId);

        System.out.println("...tasks created...");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.updateTask(firstTaskId, null, null, "IN_PROGRESS");
        manager.updateTask(secondTaskId, null, null, "DONE");
        manager.updateSubtask(subtaskId1, null, null, "IN_PROGRESS");
        manager.updateSubtask(subtaskId2, null, null, "DONE");
        manager.updateSubtask(subtaskId3, null, null, "DONE");

        System.out.println("...tasks updated...");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.deleteTaskById(firstTaskId);
        manager.deleteEpicById(firstEpicId);

        System.out.println("...tasks deleted...");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());


    }

}

