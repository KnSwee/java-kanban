package project.models;

import project.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();
    LocalDateTime endTime;

    public Epic(String name, String description, int id) {
        super(name, description, id, 0, null);
        type = TaskType.EPIC;
        duration = Duration.ZERO;
    }

    public Epic(String name, String description) {
        super(name, description, 0, null);
        type = TaskType.EPIC;
        duration = Duration.ZERO;
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtasks = new ArrayList<>(epic.subtasks);
        type = TaskType.EPIC;
        duration = Duration.ZERO;
        this.endTime = epic.getEndTime();
    }


    public void clearSubtasks() {
        subtasks.clear();
    }



    @Override
    public Epic copy() {
        return new Epic(this);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", status=" + status + '\'' +
                ", duration ='" + duration + '\'' +
                ", startTime ='" + getNoData(startTime) + '\'' +
                ", endTime ='" + getNoData(getEndTime()) + '\'' +
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
