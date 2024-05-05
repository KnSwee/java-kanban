import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int UID;
    protected Status status;

    public Task(String name, String description, int UID) {
        this.name = name;
        this.description = description;
        this.UID = UID;
        status = Status.NEW;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", UID=" + UID +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return UID == task.UID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(UID);
    }
}
