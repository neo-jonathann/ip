package monday.task;

/**
 * Represents a task managed by Monday.
 */
public class Task {
    private final String description;
    private boolean isDone;
    private final String notes;

    /**
     * Creates an incomplete task with the given description.
     */
    public Task(String description, String notes) {
        this.description = description;
        this.isDone = false;
        this.notes = notes;
    }

    /**
     * Creates a task with the given description and completion status.
     */
    public Task(String description, boolean isDone, String notes) {
        this.description = description;
        this.isDone = isDone;
        this.notes = notes;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
