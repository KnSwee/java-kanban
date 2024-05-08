import java.util.ArrayList;
import java.util.HashMap;

public class SubtaskManager {

    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Subtask createSubtask(String name, String description, int epicID) {
        int id = Manager.getID();
        subtasks.put(id, new Subtask(name, description, id, epicID));
        return getSubtaskById(id);
    }

    public HashMap<Integer, Subtask> getTasks() {
        return subtasks;
    }

    public Subtask getSubtaskById(int ID) {
        return subtasks.get(ID);
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }

    public void updateSubtask(int ID, String name, String description, String status) {
        if (!(name == null)) {
            getSubtaskById(ID).name = name;
        }
        if (!(description == null)) {
            getSubtaskById(ID).description = description;
        }
        if (!(status == null)) {
            getSubtaskById(ID).status = Status.valueOf(status);
        }
    }

    public void deleteById(int ID) {
        subtasks.remove(ID);
    }

    public void deleteByEpic(int epicID) {

        for (Subtask subtask : new ArrayList<>(subtasks.values())) {
            if (epicID == subtask.epicID) {
                subtasks.remove(subtask.ID);
            }
        }
    }
}
