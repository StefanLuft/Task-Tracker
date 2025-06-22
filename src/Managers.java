public class Managers {
    private static final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
    public static TaskManager getDefault() {
        return taskManager;
    }
    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
