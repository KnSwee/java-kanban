package project.models;

import project.enums.Status;
import project.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static project.controller.InMemoryTaskManager.FORMATTER;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status = Status.NEW;
    protected TaskType type = TaskType.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(String name, String description, Integer id, int durationInMinutes, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.startTime = startTime;
    }

    public Task(String name, String description, int durationInMinutes, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, String status, int durationInMinutes, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.valueOf(status);
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.startTime = startTime;
    }

    public Task(Task task) {
        this.name = task.name;
        this.id = task.id;
        this.description = task.description;
        this.status = task.status;
        this.duration = task.duration;
        this.startTime = task.startTime;
    }

    public Task copy() {
        return new Task(this);
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", duration ='" + duration + '\'' +
                ", startTime ='" + getNoData(startTime) + '\'' +
                ", endTime ='" + getNoData(getEndTime()) + '\'' +
                '}';
    }

    public static String getNoData(LocalDateTime time) {
        return Optional.ofNullable(time).map(t -> t.format(FORMATTER)).orElse("no data");
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

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    @Override
    public int compareTo(Task o) {
        if (this.getStartTime() == null) {
            return 1;
        }
        return Optional.ofNullable(o.getStartTime())
                .map(startTime -> this.getStartTime().compareTo(startTime))
                .orElse(-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }
}
