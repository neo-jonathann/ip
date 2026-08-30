package monday.task;

/**
 * Represents a todo task.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo task with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a todo task with the given description and completion status.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
