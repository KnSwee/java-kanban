package project.models;

import project.enums.Status;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int ID;
    protected Status status = Status.NEW;

    public Task(String name, String description, int ID) {
        this.name = name;
        this.description = description;
        this.ID = ID;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(int ID, String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.status = Status.valueOf(status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return ID == task.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getID() {
        return ID;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
