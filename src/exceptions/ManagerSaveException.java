package exceptions;

public class ManagerSaveException extends Exception {
    String message;
    public ManagerSaveException() {

    }
    public ManagerSaveException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
