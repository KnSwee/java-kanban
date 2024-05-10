package project.models;

public class Subtask extends Task {
    public Subtask(int ID, String name, String description, String status, int epicID) {
        super(ID, name, description, status);
        this.epicID = epicID;
    }

    private final int epicID;

    public Subtask(String name, String description, int ID, int epicID) {
        super(name, description, ID);
        this.epicID = epicID;
    }

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + epicID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status=" + status +
                '}';
    }

    public int getEpicID() {
        return epicID;
    }
}
