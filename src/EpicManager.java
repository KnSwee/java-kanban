import java.util.ArrayList;
import java.util.HashMap;

public class EpicManager {

    HashMap<Integer, Epic> epics = new HashMap<>();


    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public Epic createEpic(String name, String description) {
        int id = Manager.getID();
        epics.put(id, new Epic(name, description, id));
        return getEpicById(id);
    }

    public Epic getEpicById(int ID) {
        return epics.get(ID);
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void updateEpic(int ID, String name, String description) {
        if (!(name == null)) {
            getEpicById(ID).name = name;
        }
        if (!(description == null)) {
            getEpicById(ID).description = description;
        }
    }

    public void deleteById(int ID) {
        epics.remove(ID);
    }

    public void updateEpicStatus(int epicID, ArrayList<Subtask> subtasks) {
        int doneCounter = 0;
        int newCounter = 0;
        Epic epic = getEpicById(epicID);
        for (Subtask subtask : subtasks) {
            if (subtask.status == Status.DONE) {
                doneCounter++;
            } else if (subtask.status == Status.NEW) {
                newCounter++;
            }
        }
        if (newCounter == subtasks.size()) {
            epic.status = Status.NEW;
        } else if (doneCounter == subtasks.size()) {
            epic.status = Status.DONE;
        } else {
            epic.status = Status.IN_PROGRESS;
        }
    }

    public void resetEpics() {
        for (Epic epic : epics.values()) {
            epic.subtasks.clear();
            epic.status = Status.NEW;
        }
    }

}
