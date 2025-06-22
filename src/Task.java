public class Task {
    private String name;
    private Status status;
    private int taskID;

    Task(String name,
         int taskID){
        this.name = name;
        this.taskID = taskID;
    }

    public int getID() {
        return taskID;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}