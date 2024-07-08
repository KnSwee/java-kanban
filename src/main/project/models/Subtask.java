package project.models;

import project.enums.TaskType;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicID;

    public Subtask(int id, String name, String description, String status, int epicID, int durationInMinutes, LocalDateTime startTime) {
        super(id, name, description, status, durationInMinutes, startTime);
        this.epicID = epicID;
        type = TaskType.SUBTASK;

    }

    public Subtask(String name, String description, int id, int epicID, int durationInMinutes, LocalDateTime startTime) {
        super(name, description, id, durationInMinutes, startTime);
        this.epicID = epicID;
        type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, int epicID, int durationInMinutes, LocalDateTime startTime) {
        super(name, description, durationInMinutes, startTime);
        this.epicID = epicID;
        type = TaskType.SUBTASK;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicID = subtask.epicID;
        type = TaskType.SUBTASK;
    }

    @Override
    public Subtask copy() {
        return new Subtask(this);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + epicID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", status='" + status + '\'' +
                ", duration ='" + duration + '\'' +
                ", startTime ='" + startTime + '\'' +
                ", endTime ='" + getEndTime() + '\'' +
                '}';
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

}
