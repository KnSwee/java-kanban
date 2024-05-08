import java.util.ArrayList;

public class Manager {
    private static int counter = 0;
    TaskManager taskManager = new TaskManager();
    EpicManager epicManager = new EpicManager();
    SubtaskManager subtaskManager = new SubtaskManager();


    public static int getID() {
        return ++counter;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskManager.getTasks().values());
    }

    public int createTask(String name, String description) {
        return taskManager.createTask(name, description).ID;
    }

    public Task getTaskById(int ID) {
        return taskManager.getTaskById(ID);
    }

    public void deleteTasks() {
        taskManager.deleteTasks();
    }

    public void updateTask(int ID, String name, String description, String status) {
        taskManager.updateTask(ID, name, description, status);
    }

    public void deleteTaskById(int ID) {
        taskManager.deleteById(ID);
    }


    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicManager.getEpics().values());
    }

    public int createEpic(String name, String description) {
        return epicManager.createEpic(name, description).ID;
    }

    public Epic getEpicById(int ID) {
        return epicManager.getEpicById(ID);
    }

    public Epic getEpicBySubtask(int subtaskId) {
        return getEpicById(getSubtaskById(subtaskId).epicID);
    }

    public void deleteEpicById(int ID) {
        epicManager.deleteById(ID);
        subtaskManager.deleteByEpic(ID);
    }

    public void updateEpic(int ID, String name, String description) {
        epicManager.updateEpic(ID, name, description);
        epicManager.updateEpicStatus(ID, getSubtasksByEpic(ID));
    }

    public void deleteEpics() {
        epicManager.deleteEpics();
        subtaskManager.deleteSubtasks();
    }


    public ArrayList<Subtask> getSubtasksByEpic(int epicID) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtaskId : epicManager.getEpicById(epicID).subtasks) {
            subtasks.add(getSubtaskById(subtaskId));
        }
        return subtasks;
    }


    public int createSubtask(String name, String description, int epicID) {
        Subtask subtask = subtaskManager.createSubtask(name, description, epicID);
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
        epicManager.getEpicById(epicID).subtasks.add((subtask.ID));
        return subtask.ID;
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskManager.subtasks.values());
    }

    public Subtask getSubtaskById(int ID) {
        return subtaskManager.getSubtaskById(ID);
    }

    public void updateSubtask(int ID, String name, String description, String status) {
        subtaskManager.updateSubtask(ID, name, description, status);
        int epicID = subtaskManager.getSubtaskById(ID).epicID;
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    public void deleteSubtasks() {
        subtaskManager.deleteSubtasks();
        epicManager.resetEpics();
    }

    public void deleteSubtaskById(int ID) {
        int epicID = subtaskManager.getSubtaskById(ID).epicID;
        subtaskManager.deleteById(ID);
        getEpicById(epicID).subtasks.remove(ID);
        epicManager.updateEpicStatus(epicID, getSubtasksByEpic(epicID));
    }

    public void deleteSubtasksByEpic(int epicID) {
        for (int subtaskId : getEpicById(epicID).subtasks) {
            deleteSubtaskById(subtaskId);
        }
    }

}











