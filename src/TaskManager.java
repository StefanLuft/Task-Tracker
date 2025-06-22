public interface TaskManager {

    void createTask();

    void seeTasks();

    void printBasicTasks();

    void printEpics();

    void deleteAllTasks();

    void getById();

    void updateTasks();

    void deleteTaskById();

    void addSubtaskToEpic(Epic epic);

    void createBasicTask();
}