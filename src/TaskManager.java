import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();


    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public Task createTask(String name, String description) {
        int id = Manager.getID();
        tasks.put(id, new Task(name, description, id));
        return getTaskById(id);
    }

    public Task getTaskById(int ID) {
        return tasks.get(ID);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void updateTask(int ID, String name, String description, String status) {
        if (!(name == null)) {
            getTaskById(ID).name = name;
        }
        if (!(description == null)) {
            getTaskById(ID).description = description;
        }
        if (!(status == null)) {
            getTaskById(ID).status = Status.valueOf(status);
        }
    }

    public void deleteById(int ID) {
        tasks.remove(ID);
    }

}
