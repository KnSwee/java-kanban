public class Subtask extends Task {
    int epicUID;
    public Subtask(String name, String description, int UID, int epicUID) {
        super(name, description, UID);
        this.epicUID = epicUID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicUID=" + epicUID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", UID=" + UID +
                ", status=" + status +
                '}';
    }
}
