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
}
