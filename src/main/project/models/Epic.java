package project.models;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtasks = new ArrayList<>(epic.subtasks);
    }


    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public Epic copy() {
        return new Epic(this);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
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
