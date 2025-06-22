import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            if (choice == 1) {
                taskManager.seeTasks();
            } else if (choice == 2) {
                taskManager.deleteAllTasks();
            } else if (choice == 3) {
                taskManager.getById();
            } else if (choice == 4) {
                System.out.println("History of seen tasks: ");
                for (Task task : historyManager.getHistory()) {
                    System.out.println("[" + task.getID() + "] " + task.getName() + " (" + task.getStatus() +")");
                }
            } else if (choice == 5) {
                taskManager.createTask();
            } else if (choice == 6) {
                taskManager.updateTasks();
            } else if (choice == 7) {
                taskManager.deleteTaskById();
            } else if (choice == 8) {
                return;
            } else {
                System.out.println("There's no command like that.");
            }
        }
    }
    public static void showMenu() {
        System.out.println("---Main menu---");
        System.out.println("1. See list of tasks");
        System.out.println("2. Delete all tasks/epics");
        System.out.println("3. Get a task/epic by ID");
        System.out.println("4. Get the history of seen tasks");
        System.out.println("5. Create tasks/epics");
        System.out.println("6. Update tasks/epics");
        System.out.println("7. Delete task/epic by ID");
        System.out.println("8. Exit");
        System.out.println("-------------");
    }
}