import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;
import service.HistoryManager;
import service.TaskManager;
import util.Managers;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = taskManager.getHistoryManager();

        Task task1 = new Task("Draw", "Create a painting of building");
        Epic epic1 = new Epic("Sleep routine", "Prepare for sleep");
        Subtask subtask1 = new Subtask("Shower", "shower lol", epic1);
        Subtask subtask2 = new Subtask("Wash mouth", "Take a brush", epic1);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Task updatedTask1 = new Task("Sing", "Sing Noize MC's song");
        Epic updatedEpic1 = new Epic("IT", "Learn PLs");
        Subtask updatedSubtask1 = new Subtask("Java", "Presentation 11", updatedEpic1);
        Subtask updatedSubtask2 = new Subtask("Android", "UI", updatedEpic1);

        taskManager.update(1, updatedTask1);
        taskManager.update(2, updatedEpic1);
        taskManager.update(3, updatedSubtask1);
        taskManager.update(4, updatedSubtask2);

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);

        taskManager.getTasks();
        taskManager.getEpics();
        taskManager.getSubtasks();
        taskManager.getSubtasksByEpic(updatedEpic1);

        taskManager.removeSubtaskById(3);


        taskManager.getEpicById(2);
        taskManager.getEpicById(2);
        taskManager.getEpicById(2);
        taskManager.getEpicById(2);

        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);

        System.out.println(historyManager.getHistory());

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager);
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getSubtasks());
        System.out.println(fileBackedTasksManager.getHistoryManager().getHistory());
    }
}