public class TaskModel {
    protected String name;
    protected String description;
    protected int UID;
    protected Status status;

    protected TaskType taskType;

    protected String typeDescription;

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

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", UID=" + UID +
                ", status=" + status +
                ", taskType=" + taskType +
                ", typeDescription='" + typeDescription + '\'' +
                '}';
    }
}
