package project.models;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description, int ID) {
        super(name, description, ID);
    }

    public Epic(String name, String description) {
        super(name, description);
    }


    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status=" + status +
                '}';
    }

    public void addSubtask(int subtaskID) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
        }
        subtasks.add(subtaskID);
    }

    public void removeSubtask(int subtaskID) {
        subtasks.remove((Integer) subtaskID);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }
}
