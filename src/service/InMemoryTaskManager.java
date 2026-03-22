package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;

    protected int uniqueId = 1;

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public int getUniqueId() {
        return uniqueId++;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        task.setId(getUniqueId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        epic.setId(getUniqueId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) throws ManagerSaveException {
        subtask.setId(getUniqueId());
        Epic epic = subtask.getEpic();
        if (epics.containsKey(epic.getId())) {
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            epic.updateStatus();
        }
    }

    @Override
    public void update(int id, Task updatedTask) throws ManagerSaveException {
        tasks.put(id, updatedTask);
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epics.get(id));
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtask;
        }
        return null;
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException {
        Task task = tasks.get(id);
        if (task != null) {
            tasks.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epic.getSubtasks().clear();
            epics.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) throws ManagerSaveException {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            epic.getSubtasks().remove(subtask);
            epic.updateStatus();
            subtasks.remove(id);
        }

    }

    @Override
    public List<Task> getTasks() throws ManagerSaveException {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getEpics() throws ManagerSaveException {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getSubtasks() throws ManagerSaveException {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("No epic found!");
            return new ArrayList<>();
        }
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public void removeAll() throws IOException, ManagerSaveException {
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.updateStatus();
        }
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }
}