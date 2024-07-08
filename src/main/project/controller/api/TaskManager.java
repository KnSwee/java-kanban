package project.controller.api;

import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.util.ArrayList;
import java.util.TreeSet;

public interface TaskManager {


    ArrayList<Task> getTasks();

    int createTask(Task task);

    Task getTaskById(int id);

    void deleteTasks();

    void updateTask(Task task);

    void deleteTaskById(int id);

    ArrayList<Epic> getEpics();

    int createEpic(Epic epic);

    Epic getEpicById(int id);

    Epic getEpicBySubtask(int subtaskId);

    void deleteEpicById(int id);

    void updateEpic(Epic epic);

    void deleteEpics();

    ArrayList<Subtask> getSubtasksByEpic(int epicID);

    int createSubtask(Subtask subtask);

    ArrayList<Subtask> getSubtasks();

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    void deleteSubtasks();

    void deleteSubtaskById(int id);

    void deleteSubtasksByEpic(int epicID);

    ArrayList<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}
