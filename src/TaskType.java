public enum TaskType {
    TASK("Задача"),
    EPIC("Эпик"),
    SUBTASK("Подзадача");

    final String visualization;

    TaskType(String visualization) {
        this.visualization = visualization;
    }

    public String getVisualization() {
        return visualization;
    }

    @Override
    public String toString() {
        return "TaskType{" +
                "visualization='" + visualization + '\'' +
                '}';
    }
}
