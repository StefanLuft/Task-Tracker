import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> seenTasks = new ArrayList<>(10);

    public List<Task> getHistory() {
        return seenTasks;
    }

    public void add(Task task) {
        if (seenTasks.size() >= 10) {
            seenTasks.remove(0);
        }
        seenTasks.add(task);
    }
}
