package project.models;

import project.enums.Status;
import project.enums.TaskType;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status = Status.NEW;
    protected TaskType type = TaskType.TASK;

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.valueOf(status);
    }

    public Task(Task task) {
        this.name = task.name;
        this.id = task.id;
        this.description = task.description;
        this.status = task.status;
    }

    public Task copy() {
        return new Task(this);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }
}
