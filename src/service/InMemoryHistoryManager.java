package service;

import model.Task;
import util.CustomLinkedList;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private final CustomLinkedList<Task> seenTasks = new CustomLinkedList<>();
    private final HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            seenTasks.removeNode(nodeMap.get(task.getId()));
        }
        Node<Task> node = seenTasks.linkLast(task);
        nodeMap.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = nodeMap.get(id);
        if (node != null) {
            seenTasks.removeNode(node);
            nodeMap.remove(id, node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return seenTasks.getTasks();
    }
}