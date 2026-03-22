package model;

public class Subtask extends Task {

    private Epic epic;

    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.epic = epic;
    }
    @Override
    public String toString() {
        return String.format("%d, %s, %s, %s, %s, %d", getId(), getType(), getTitle(), getStatus(), getDescription(), epic.getId());
    }
    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        epic.updateStatus();
    }

    public Epic getEpic() {
        return epic;
    }

}