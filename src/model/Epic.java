package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasks = new HashMap<>();
        setType(Type.EPIC);
    }

    public void updateStatus() {
        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : getSubtasks()) {
            if (!subtask.getStatus().equals(Status.NEW)) {
                allNew = false;
            }
            if (!subtask.getStatus().equals(Status.DONE)) {
                allDone = false;
            }

            if (!allNew && !allDone) {
                break;
            }

            if (subtasks.isEmpty() || allNew) {
                setStatus(Status.NEW);
            } else if (allDone) {
                setStatus(Status.DONE);
                setTime(LocalTime.now());
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }


    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void removeSubtaskById(int id) {
        subtasks.remove(id);
    }
}