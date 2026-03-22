package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    
    int getUniqueId();

    void createTask(Task task) throws ManagerSaveException;

    void createEpic(Epic epic) throws ManagerSaveException;

    void createSubtask(Subtask subtask) throws ManagerSaveException;

    void update(int id, Task updatedTask) throws ManagerSaveException;

    Task getTaskById(int id) throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    Subtask getSubtaskById(int id) throws ManagerSaveException;

    void removeTaskById(int id) throws ManagerSaveException;

    void removeEpicById(int id) throws ManagerSaveException;

    void removeSubtaskById(int id) throws ManagerSaveException;

    List<Task> getTasks() throws ManagerSaveException;

    List<Task> getEpics() throws ManagerSaveException;

    List<Task> getSubtasks() throws ManagerSaveException;

    ArrayList<Subtask> getSubtasksByEpic(Epic epic);

    void removeAll() throws IOException, ManagerSaveException;

    HistoryManager getHistoryManager();

}
