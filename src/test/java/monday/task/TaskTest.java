package monday.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for Task.
 */
class TaskTest {
    @Test
    void markAsDone_marksTaskAndUpdatesDisplay() {
        Task task = new Task("read chapter");

        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
        assertEquals("[X] read chapter", task.toString());
    }

    @Test
    void markAsNotDone_unmarksPreviouslyCompletedTask() {
        Task task = new Task("read chapter", true);

        task.markAsNotDone();

        assertEquals(" ", task.getStatusIcon());
        assertEquals("[ ] read chapter", task.toString());
    }
}
