import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        boolean continueRun = true;
        while (continueRun) {
            printMenu();
            int select = scanner.nextInt();
            switch (select) {
                case 1:
                    taskManager.getAllTasks();
                    continueRun = continueRunCheck();
                    break;
                case 2:
                    System.out.println("Введите id задачи:");
                    int selectId = scanner.nextInt();
                    taskManager.getTaskById(selectId);
                    continueRun = continueRunCheck();
                    break;
                case 3:
                    taskManager.createTask();
                    continueRun = continueRunCheck();
                    break;
                case 4:
                    System.out.println("Введите id задачи, которую хотите обновить:");
                    int updatedId = scanner.nextInt();
                    System.out.println("Введите новое имя задачи. Если не хотите менять имя, введите \"0\"");
                    String newName = scanner.nextLine();
                    System.out.println("Введите новое описание задачи. Если не хотите менять описание, введите \"0\"");
                    String newDescription = scanner.nextLine();
                    System.out.println("Введите новый статус задачи:");
                    System.out.println("(Допустимые значения : NEW, IN_PROGRESS, DONE)");
                    Status newStatus = Status.valueOf(scanner.nextLine());
                    TaskModel task = new TaskModel();
                    task.setName(newName);
                    task.setDescription(newDescription);
                    task.setUID(updatedId);
                    task.setStatus(newStatus);
                    taskManager.updateTask(task);
                    continueRun = continueRunCheck();
                    break;
                case 5:

                    continueRun = continueRunCheck();
                    break;
                case 6:

                    continueRun = continueRunCheck();
                    break;
                default:
                    System.out.println("Такой команды не существует, попробуйте снова.");
            }
        }

    }



    private static boolean continueRunCheck() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Хотите сделать что-то ещё?");
        System.out.println("1. Да");
        System.out.println("2. Выход");
        int select = scanner.nextInt();
        return select == 1;
    }


    public static void printMenu() {
        System.out.println("Что вы хотите сделать?");
        System.out.println(("(Введите номер пункта меню)"));
        System.out.println("1. Вывести список задач.");
        System.out.println("2. Вывести задачу по id.");
        System.out.println("3. Создать задачу.");
        System.out.println("4. Обновить задачу.");
        System.out.println("5. Удалить все задачи.");
        System.out.println("6. Удалить задачу по id.");
    }

}
