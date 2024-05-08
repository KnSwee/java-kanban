import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description, int ID) {
        super(name, description, ID);
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
}
