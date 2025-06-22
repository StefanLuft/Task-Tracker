import java.util.*;
public class InMemoryTaskManager implements TaskManager {
    Scanner scanner = new Scanner(System.in);

    Map<Integer, Task> basicTasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    Map<Integer, Subtask> subtasks = new HashMap<>();

    int epicID = 0;
    int taskID = 0;
    int subtaskID = 0;

    InMemoryHistoryManager historyManager;

    InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void createTask() {
        System.out.println("What kind of task do you want to create?");
        System.out.println("1. Basic Task");
        System.out.println("2. Epic Task");
        System.out.println("3. Extend existing epic");
        System.out.println("4. Return");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            createBasicTask();
        } else if (choice == 2) {
            System.out.println("Enter the name of the Epic");
            String nameOfEpic = scanner.nextLine();

            int taskID = ++epicID;
            HashMap<Integer, Subtask> tasksOfEpic = new HashMap<>();
            Epic epicNEW = new Epic(nameOfEpic, taskID, tasksOfEpic);
            epics.put(epicNEW.getID(), epicNEW);
            epicNEW.setStatus(Status.NEW);

            System.out.println("Do you want to add subtasks to the epic?");
            System.out.println("1. Yes");
            System.out.println("2. Return");
            int answer = scanner.nextInt();
            scanner.nextLine();
            if (answer == 1) {
                addSubtaskToEpic(epicNEW);
                boolean addingSubs = true;
                while (addingSubs) {
                    System.out.println("Do you want to add subtasks to the epic?");
                    System.out.println("1. Yes");
                    System.out.println("2. Return");
                    int answer2 = scanner.nextInt();
                    scanner.nextLine();

                    if (answer2 == 1) {
                        addSubtaskToEpic(epicNEW);
                    } else if (answer2 == 2) {
                        addingSubs = false;
                    } else {
                        System.out.println("There's no command like that.");
                    }
                }
            } else if (answer == 2) {
                return;
            } else {
                System.out.println("There's no command like that.");
            }

        } else if (choice == 3) {
            System.out.println("Choose your epic you want to make changes in");
            for (Epic epic : epics.values()) {
                System.out.println("[" + epic.getID() + "] " + epic.getName() + " (" + epic.getStatus() +")");
            }

            System.out.println("Enter the ID of the epic you want to extend:");
            int chosenID = scanner.nextInt();
            scanner.nextLine();

            Epic selectedEpic = epics.get(chosenID);

            if (selectedEpic == null) {
                System.out.println("Epic not found");
                return;
            }

            addSubtaskToEpic(selectedEpic);
        } else if (choice == 4) {
            return;
        } else {
            System.out.println("Error");
        }
    }

    public void seeTasks() {
        printBasicTasks();
        printEpics();
    }

    public void printBasicTasks() {
        if (basicTasks.isEmpty()) {
            System.out.println("List of basic tasks is empty.");
        } else {
            System.out.println("List of basic tasks: ");
            for (Task task : basicTasks.values()) {
                System.out.println("[" + task.getID() + "] " + task.getName() + " (" + task.getStatus() +")");
            }
        }
    }

    public void printEpics() {
        if (epics.isEmpty()) {
            System.out.println("List of epics is empty.");
        } else {
            System.out.println("List of epics: ");
            for (Epic epic : epics.values()) {
                System.out.println("[" + epic.getID() + "] " + epic.getName() + " (" + epic.getStatus() +")");
                System.out.println("Subtasks: ");
                for (Subtask subtask : epic.tasksOfEpic.values()) {
                    System.out.println("[" + subtask.getID() + "] " + subtask.getName() + " (" + subtask.getStatus() +")");
                }
            }
        }
    }

    public void deleteAllTasks() {
        System.out.println("What do want to delete? ");
        System.out.println("1. All tasks");
        System.out.println("2. All epics");
        int answer = scanner.nextInt();
        if (answer == 1) {
            System.out.println("Are you sure that you want to delete all tasks?");
            System.out.println("1. Definitely");
            System.out.println("2. No");

            int choice = scanner.nextInt();

            if (choice == 1) {
                basicTasks.clear();
                System.out.println("All tasks were deleted");
            } else if (choice == 2) {
                return;
            }
        } else if (answer == 2) {
            System.out.println("Are you sure that you want to delete all epics?");
            System.out.println("1. Definitely");
            System.out.println("2. No");

            int choice = scanner.nextInt();

            if (choice == 1) {
                epics.clear();
                subtasks.clear();
                System.out.println("All epics were deleted");
            } else if (choice == 2) {
                return;
            } else {
                System.out.println("There's no command like that.");
            }
        } else {
            System.out.println("There's no command like that.");
        }
    }

    public void getById() {
        System.out.println("1. Get a task");
        System.out.println("2. Get an epic");
        System.out.println("3. Get a subtask");
        System.out.println("4. Return");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Choose task's ID to proceed.");
            int chosenID = scanner.nextInt();
            scanner.nextLine();
            Task task = basicTasks.get(chosenID);
            if (task == null) {
                System.out.println("Task not found");
                return;
            }
            System.out.println("[" + task.getID() + "] " + task.getName() + " (" + task.getStatus() +")");
            historyManager.add(task);

        } else if (choice == 2) {
            System.out.println("Choose epic's ID to proceed.");
            int chosenID = scanner.nextInt();
            Epic epic = epics.get(chosenID);
            scanner.nextLine();
            if (epic == null) {
                System.out.println("Epic not found");
                return;
            }
            System.out.println("[" + epic.getID() + "] " + epic.getName() + " (" + epic.getStatus() +")");
            historyManager.add(epic);
            System.out.println("Subtasks: ");
            for (Subtask subtask : epic.tasksOfEpic.values()) {
                System.out.println("[" + subtask.getID() + "] " + subtask.getName() + " (" + subtask.getStatus() +")");
            }
        } else if (choice == 3) {
            System.out.println("Choose subtask's's ID to proceed.");
            int chosenID = scanner.nextInt();
            Subtask subtask = subtasks.get(chosenID);
            scanner.nextLine();
            if (subtask == null) {
                System.out.println("Subtask not found");
                return;
            }
            System.out.println("[" + subtask.getID() + "] " + subtask.getName() + " (" + subtask.getStatus() +")");
            historyManager.add(subtask);
        } else if (choice == 4) {
            return;
        }
    }

    public void updateTasks() {
        seeTasks();
        int done = 0;
        int inProgress = 0;
        int total = 0;
        System.out.println("1. Update a task");
        System.out.println("2. Update an epic");
        System.out.println("3. Return");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Choose task's ID to proceed.");
            int chosenID = scanner.nextInt();
            Task task = basicTasks.get(chosenID);
            System.out.println("[" + task.getID() + "] " + task.getName() + " (" + task.getStatus() +")");

            System.out.println("Select the status that you want to set.");
            System.out.println("1. IN_PROGRESS");
            System.out.println("2. DONE");

            int answer = scanner.nextInt();

            if (answer == 1) {
                task.setStatus(Status.IN_PROGRESS);
                System.out.println("Status was set.");
            } else if (answer == 2) {
                task.setStatus(Status.DONE);
                System.out.println("Status was set.");
            } else {
                System.out.println("There's no status like that");
            }
        } else if (choice == 2) {
            System.out.println("Choose epic's ID to proceed.");
            int chosenID = scanner.nextInt();
            Epic epic = epics.get(chosenID);
            System.out.println("[" + epic.getID() + "] " + epic.getName() + " (" + epic.getStatus() +")");
            System.out.println("Subtasks: ");
            for (Subtask subtask : epic.tasksOfEpic.values()) {
                System.out.println("[" + subtask.getID() + "] " + subtask.getName() + " (" + subtask.getStatus() +")");
            }
            System.out.println("Select the subtask by the ID.");
            int chosenSubID = scanner.nextInt();

            Subtask subtask1 = epic.tasksOfEpic.get(chosenSubID);

            System.out.println("Select the status that you want to set to the subtask: ");

            System.out.println("1. IN_PROGRESS");
            System.out.println("2. DONE");

            int answer = scanner.nextInt();

            if (answer == 1) {
                subtask1.setStatus(Status.IN_PROGRESS);
                System.out.println("Status was set.");
            } else if (answer == 2) {
                subtask1.setStatus(Status.DONE);
                System.out.println("Status was set.");
            } else {
                System.out.println("There's no status like that");
            }


            for (Subtask subtask : epic.tasksOfEpic.values()) {
                if (subtask.getID() == epic.getID()) {
                    total++;
                    if (subtask.getStatus() == Status.IN_PROGRESS) {
                        inProgress++;
                    } else if (subtask.getStatus() == Status.DONE) {
                        done++;
                    }
                }
            }
            if (total == 0) {
                epic.setStatus(Status.NEW);
            } else if (total == done) {
                epic.setStatus(Status.DONE);
            } else if (inProgress > 0 || done > 0) {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void deleteTaskById() {
        seeTasks();
        System.out.println("1. Delete a task");
        System.out.println("2. Delete an epic");
        System.out.println("3. Return");
        int choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Choose task's ID to proceed.");
            int chosenID = scanner.nextInt();
            Task task = basicTasks.remove(chosenID);
            if (task == null) {
                System.out.println("Task not found");
                return;
            }
            System.out.println(task.getName() + " was removed from the list.");
        } else if (choice == 2) {
            System.out.println("Choose epic's ID to proceed.");
            int chosenID = scanner.nextInt();
            Epic epic = epics.remove(chosenID);
            if (epic == null) {
                System.out.println("Epic not found");
                return;
            }
            System.out.println(epic.getName() + " was removed from the list.");
            System.out.println("Deleted subtasks: ");
            for (Subtask subtask : epic.tasksOfEpic.values()) {
                subtasks.remove(subtask.getID());
            }
        } else if (choice == 3) {
            return;
        }
    }

    public void addSubtaskToEpic(Epic epic) {
        System.out.println("Enter the name of the subtask");
        String name = scanner.nextLine();

        int subtaskid = ++subtaskID;
        Subtask subtask = new Subtask(name,subtaskid);
        subtask.setStatus(Status.NEW);
        subtasks.put(subtask.getID(), subtask);
        epic.tasksOfEpic.put(subtask.getID(), subtask);

        System.out.println("Subtask added: " + subtask.getName());
    }

    public void createBasicTask() {
        System.out.println("Enter the name of the task");
        String name = scanner.nextLine();

        Task taskNEW = new Task(name, ++taskID);
        basicTasks.put(taskNEW.getID(), taskNEW);
        taskNEW.setStatus(Status.NEW);

        System.out.println("Your task was created.");
        System.out.println("[" + taskNEW.getID() + "] " + taskNEW.getName() + " (" + taskNEW.getStatus() +")");
    }
}