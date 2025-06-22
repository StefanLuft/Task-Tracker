import java.util.Map;

public class Epic extends Task {
    protected Map<Integer, Subtask> tasksOfEpic;
    Epic(String name,
         int taskID,
         Map<Integer, Subtask> tasksOfEpic){
        super(name, taskID);
        this.tasksOfEpic = tasksOfEpic;
    }
}
