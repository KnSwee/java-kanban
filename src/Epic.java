public class Epic extends Task {
    public Epic(String name, String description, int UID) {
        super(name, description, UID);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", UID=" + UID +
                ", status=" + status +
                '}';
    }
}
