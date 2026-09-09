package monday.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * Tests for Deadline.
 */
class DeadlineTest {
    @Test
    void toString_withTime_displaysFormattedDateAndTime() {
        Deadline deadline = new Deadline(
                "submit report",
                LocalDate.of(2026, 8, 30),
                LocalTime.of(14, 30),
                "Phantom Note");

        assertEquals("[D][ ] submit report (by: 30 Aug 2026 1430)",
                deadline.toString());
    }

    @Test
    void toString_withoutTime_displaysOnlyFormattedDate() {
        Deadline deadline = new Deadline(
                "submit report",
                LocalDate.of(2026, 8, 30),
                null,
                "Phantom Note");

        assertEquals("[D][ ] submit report (by: 30 Aug 2026)",
                deadline.toString());
    }
}
