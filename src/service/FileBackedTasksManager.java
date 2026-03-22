package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final static String HEADER = "id,type,name,status,description,epic";
    private final File file = new File("manager.txt");
    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }
    @Override
    public void createSubtask(Subtask subtask) throws ManagerSaveException {
        super.createSubtask(subtask);
        save();
    }
    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }
    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        super.createEpic(epic);
        save();
    }
    @Override
    public void update(int id, Task updatedTask) throws ManagerSaveException {
        super.update(id, updatedTask);
        save();
    }
    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }
    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }
    @Override
    public void removeTaskById(int id) throws ManagerSaveException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) throws ManagerSaveException {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public List<Task> getTasks() throws ManagerSaveException {
        List<Task> tasks = super.getTasks();
        save();
        return tasks;
    }

    @Override
    public List<Task> getEpics() throws ManagerSaveException {
        List<Task> epics = super.getEpics();
        save();
        return epics;
    }

    @Override
    public List<Task> getSubtasks() throws ManagerSaveException {
        List<Task> subtasks = super.getSubtasks();
        save();
        return subtasks;
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        return super.getSubtasksByEpic(epic);
    }

    @Override
    public void removeAll() throws IOException, ManagerSaveException {
        super.removeAll();
        save();
    }

    private void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(HEADER + "\n");

            for (Task task : super.getTasks()) {
                fileWriter.write(task.toString());
            }
            for (Task epic : super.getEpics()) {
                fileWriter.write(epic.toString());
            }
            for (Task subtask : super.getSubtasks()) {
                fileWriter.write(subtask.toString());
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось записать данные в файл");
        }
    }
    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(new InMemoryHistoryManager());
        if (!file.exists()) return fileManager;
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int maxId = 0;
            boolean isHistory = false;

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    isHistory = true;
                    continue;
                }
                if (isHistory) {
                    List<Integer> historyIds = fileManager.historyFromString(line);
                    for (Integer id : historyIds) {
                        Task task = null;
                        if (fileManager.tasks.containsKey(id)) {
                            task = fileManager.tasks.get(id);
                        } else if (fileManager.epics.containsKey(id)) {
                            task = fileManager.epics.get(id);
                        } else if (fileManager.subtasks.containsKey(id)) {
                            task = fileManager.subtasks.get(id);
                        }
                        if (task != null) {
                            fileManager.getHistoryManager().add(task);
                        }
                    }
                    break;
                }

                Task task = fileManager.fromString(line);
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }

                switch (task.getType()) {
                    case EPIC -> fileManager.epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> {
                        fileManager.subtasks.put(task.getId(), (Subtask) task);
                        Epic epic = fileManager.epics.get(((Subtask) task).getEpic().getId());
                        if (epic != null) {
                            epic.addSubtask((Subtask) task);
                        }
                    }
                    case TASK -> fileManager.tasks.put(task.getId(), task);
                }
                fileManager.uniqueId = maxId + 1;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось записать файл");
        }
        return fileManager;
    }
    public Task fromString(String value) throws ManagerSaveException {
        String[] parts = value.split(",");
        if (parts.length > 7) {
            throw new RuntimeException("Недопустимый формат задачи");
        }
        int id = Integer.parseInt(parts[0].trim());
        Type type = Type.valueOf(parts[1].trim());
        String title = parts[2].trim();
        Status status = Status.valueOf(parts[3].trim());
        String description = parts[4].trim();
        switch (type) {
            case EPIC -> {
                Epic epic = new Epic(title, description);
                epic.setStatus(status);
                epic.setId(id);
                return epic;
            }
            case SUBTASK -> {
                Epic epic = super.getEpicById(Integer.parseInt(parts[5].trim()));
                Subtask subtask = new Subtask(title, description, epic);
                subtask.setStatus(status);
                subtask.setId(id);
                return subtask;
            }
        }
        Task task = new Task(title, description);
        task.setStatus(status);
        task.setId(id);
        return task;
    }
    public static String historyToString(HistoryManager manager) {
        StringBuilder builder = new StringBuilder();
        List<Task> history = manager.getHistory();
        for (int i = 0; i < history.size(); i++) {
            builder.append(history.get(i).getId());
            if (i < history.size() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
    public List<Integer> historyFromString(String value) {
        String[] parts = value.split(",");
        List<Integer> history = new ArrayList<>();
        if (value.isBlank()) return history;
        for (String part : parts) {
            history.add(Integer.parseInt(part.trim()));
        }
        return history;
    }
}
