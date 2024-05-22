package project.controller.api;

import project.models.Epic;
import project.models.Subtask;
import project.models.Task;

import java.util.ArrayList;

public interface TaskManager {


    ArrayList<Task> getTasks();

    int createTask(Task task);

    Task getTaskById(int ID);

    void deleteTasks();

    void updateTask(Task task);

    void deleteTaskById(int ID);

    ArrayList<Epic> getEpics();

    int createEpic(Epic epic);

    Epic getEpicById(int ID);

    Epic getEpicBySubtask(int subtaskId);

    void deleteEpicById(int ID);

    void updateEpic(Epic epic);

    void deleteEpics();

    ArrayList<Subtask> getSubtasksByEpic(int epicID);

    int createSubtask(Subtask subtask);

    ArrayList<Subtask> getSubtasks();

    Subtask getSubtaskById(int ID);

    void updateSubtask(Subtask subtask);

    void deleteSubtasks();

    void deleteSubtaskById(int ID);

    void deleteSubtasksByEpic(int epicID);

    ArrayList<Task> getHistory();
}
