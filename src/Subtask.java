public class Subtask extends Task {
    int epicID;

    public Subtask(String name, String description, int ID, int epicID) {
        super(name, description, ID);
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
}
