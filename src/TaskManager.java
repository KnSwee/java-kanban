import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    Scanner scanner = new Scanner(System.in);
    int counter = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public void createTask() {
        printSelectMenu(); //todo ПЕРЕНЕСТИ СКАНЕРЫ В МЕЙН
        int select = scanner.nextInt();
        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        int current;
        switch (select) {
            case 1:
                current = counter + 1;
                tasks.put(current, new Task(name, description,current));
                System.out.println("Задача создана. Id: " + current);
                break;
            case 2:
                current = counter + 1;
                epics.put(current, new Epic(name, description, current));
                System.out.println("Эпик создан. Id: " + current);
                break;
            case 3:
                while(true) {
                    System.out.println("Введите id эпика, для которого вы хотите создать подзадачу:");
                    int epicID = scanner.nextInt();
                    if (!epics.containsKey(epicID)) {
                        System.out.println("Такого эпика не существует. Попробуйте снова.");
                    } else {
                        current = counter + 1;
                        subtasks.put(current, new Subtask(name, description, current, epicID));
                        System.out.println("Подзадача создана. Id: " + current);
                        break;
                    }
                }
                break;
            default:
                System.out.println("Такой команды нет :(");
        }
        printCurrentTasksNumber();
    }

    public ArrayList<TaskModel> getAllTasks() {
        ArrayList<TaskModel> allTasks = new ArrayList<>();
        for (Integer UID : tasks.keySet()) {
            TaskModel taskModel = getTaskModel(UID);
            allTasks.add(taskModel);
        }
        for (Integer UID : epics.keySet()) {
            TaskModel taskModel = getEpicModel(UID);
            allTasks.add(taskModel);
        }
        for (Integer UID : subtasks.keySet()) {
            TaskModel taskModel = getSubtaskModel(UID);
            allTasks.add(taskModel);
        }
        System.out.println(allTasks); //todo delete this
        return allTasks;
    }

    public TaskModel getTaskById(int UID) {
        if (tasks.containsKey(UID)) {
            System.out.println(getTaskModel(UID)); //todo delete this
            return getTaskModel(UID);
        } else if (epics.containsKey(UID)) {
            System.out.println(getEpicModel(UID)); //todo delete this
            return getEpicModel(UID);
        } else if (subtasks.containsKey(UID)) {
            System.out.println(getSubtaskModel(UID)); //todo delete this
            return getSubtaskModel(UID);
        } else {
            System.out.println("Задачи с таким id нет в трекере.");
            return null;
        }
    }

    public void updateTask(TaskModel taskModel) {
        TaskModel task = getTaskById(taskModel.UID);
        if (!"0".equals(taskModel.name)) {
            task.name = taskModel.name;
        }
        if (!"0".equals(taskModel.description)) {
            task.description = taskModel.description;
        }
        updateStatus(taskModel);
    }

    public ArrayList<Subtask> getSubtasksByEpic(int epicUID) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask task : subtasks.values()) {
            if (task.epicUID == epicUID) {
                subtasksByEpic.add(task);
            }
        }
        return subtasksByEpic;
    }





    private void updateStatus(TaskModel task) {
        if (epics.containsKey(task.UID)) {
            System.out.println("Нельзя самостоятельно изменить статус эпика.");
            return;
        } else if (subtasks.containsKey(task.UID)) {
            getTaskById(task.UID).status = task.status;
            int epicUID = subtasks.get(task.UID).epicUID;
            ArrayList<Subtask> subtasksByEpic = getSubtasksByEpic(epicUID);
            boolean allDone = true;
            for (Subtask subtask : subtasksByEpic) {
                if (subtask.status == Status.IN_PROGRESS) {
                    epics.get(epicUID).status = Status.IN_PROGRESS;
                    break;
                } else if (subtask.status == Status.NEW) {
                    allDone = false;
                }
            }
            if (allDone) {
                epics.get(epicUID).status = Status.DONE;
            } else {
                epics.get(epicUID).status = Status.NEW;
            }
        } else {
            getTaskById(task.UID).status = task.status;
        }
    }


    private TaskModel getSubtaskModel(Integer UID) {
        Subtask subtask = subtasks.get(UID);
        TaskModel taskModel = new TaskModel();
        fillTask(UID, taskModel, subtask);
        taskModel.taskType = TaskType.SUBTASK;
        taskModel.typeDescription = TaskType.SUBTASK.visualization;
        return taskModel;
    }

    private TaskModel getEpicModel(Integer UID) {
        Epic epic = epics.get(UID);
        TaskModel taskModel = new TaskModel();
        fillTask(UID, taskModel, epic);
        taskModel.taskType = TaskType.EPIC;
        taskModel.typeDescription = TaskType.EPIC.visualization;
        return taskModel;
    }

    private TaskModel getTaskModel(Integer UID) {
        Task task = tasks.get(UID);
        TaskModel taskModel = new TaskModel();
        fillTask(UID, taskModel, task);
        taskModel.taskType = TaskType.TASK;
        taskModel.typeDescription = TaskType.TASK.visualization;
        return taskModel;
    }

    private static void fillTask(Integer UID, TaskModel taskModel, Task task) {
        taskModel.UID = UID;
        taskModel.name = task.name;
        taskModel.description = task.description;
        taskModel.status = task.status;
    }

    private void printCurrentTasksNumber() {
        System.out.println("Текущее количество задач: " + counter);
    }

    private void printSelectMenu() {
        System.out.println("Что хотите создать?");
        System.out.println("(введите номер пункта меню)");
        System.out.println("1. Задача");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадача");
    }



}
