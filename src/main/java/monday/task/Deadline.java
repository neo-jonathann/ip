package monday.task;

import java.time.LocalDate;
import java.time.LocalTime;

import monday.util.DateTimeFormat;

/**
 * Represents a task that must be completed by a specific date and optional time.
 */
public class Deadline extends Task {
    private final LocalDate deadlineDate;
    private final LocalTime deadlineTime;

    public Deadline(String description, LocalDate deadlineDate, LocalTime deadlineTime) {
        super(description);
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    public Deadline(String description, boolean isDone, LocalDate deadlineDate, LocalTime deadlineTime) {
        super(description, isDone);
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public LocalTime getDeadlineTime() {
        return deadlineTime;
    }

    @Override
    public String toString() {
        String deadline = deadlineDate.format(DateTimeFormat.DISPLAY_DATE.getFormatter());

        if (deadlineTime != null) {
            deadline += " " + deadlineTime.format(DateTimeFormat.DISPLAY_TIME.getFormatter());
        }

        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }
}
