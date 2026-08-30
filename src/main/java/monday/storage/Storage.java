package monday.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import monday.task.Deadline;
import monday.task.Event;
import monday.task.Task;
import monday.task.TaskList;
import monday.task.Todo;
import monday.util.DateTimeFormat;

public class Storage {
    private static final Path FILE_PATH = Path.of("data", "monday.txt");

    /**
     * Loads saved tasks. Returns an empty list when no save files exists yet.
     */
    public static ArrayList<Task> loadTask() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (Files.notExists(FILE_PATH)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(FILE_PATH);

            for (String line : lines) {
                if (!line.isBlank()) {
                    tasks.add(createTask(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Sorry, I could not load your tasks.");
        }

        return tasks;
    }

    public static void saveTask(TaskList tasks) {
        ArrayList<String> savedTasks = new ArrayList<>();

        for (Task task : tasks) {
            savedTasks.add(convertToFileFormat(task));
        }

        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.write(FILE_PATH, savedTasks);
        } catch (IOException e) {
            System.out.println("Sorry, I could not save your tasks.");
        }
    }

    /**
     * Converts one saved line into the appropriate Task object.
     */
    private static Task createTask(String line) {
        String[] parts = line.split("\\|", -1);

        String taskType = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        Task task;

        switch (taskType) {
            case "T":
                task = new Todo(parts[2].trim(), isDone);
                break;
            case "D":
                LocalDate deadlineDate = LocalDate.parse(parts[3].trim());
                LocalTime deadlineTime = null;
                String savedTime = parts[4].trim();
                if (!savedTime.isEmpty()) {
                    deadlineTime = LocalTime.parse(savedTime, DateTimeFormat.INPUT_TIME.getFormatter());
                }
                task = new Deadline(parts[2].trim(), isDone, deadlineDate, deadlineTime);
                break;
            case "E":
                LocalDate eventDate1 = LocalDate.parse(parts[3].trim());
                LocalTime eventTime1 = null;
                String savedTime1 = parts[4].trim();
                if (!savedTime1.isEmpty()) {
                    eventTime1 = LocalTime.parse(savedTime1, DateTimeFormat.INPUT_TIME.getFormatter());
                }

                LocalDate eventDate2 = LocalDate.parse(parts[5].trim());
                LocalTime eventTime2 = null;
                String savedTime2 = parts[6].trim();
                if (!savedTime2.isEmpty()) {
                    eventTime2 = LocalTime.parse(savedTime2, DateTimeFormat.INPUT_TIME.getFormatter());
                }

                task = new Event(parts[2].trim(), isDone, eventDate1, eventTime1, eventDate2, eventTime2);
                break;
            default:
                throw new IllegalArgumentException("Invalid task type in save file.");
        }

        return task;
    }

    /**
     * Converts one Task object into a line suitable for the save file.
     */
    private static String convertToFileFormat(Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + status + " | " + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            String savedTime = deadline.getDeadlineTime() == null
                    ? ""
                    : deadline.getDeadlineTime().format(DateTimeFormat.INPUT_TIME.getFormatter());
            return "D | " + status + " | " + task.getDescription()
                    + " | " + deadline.getDeadlineDate() + " | " +  savedTime;
        }

        Event event = (Event) task;
        String savedTime1 = event.getStartTime() == null
                ? ""
                : event.getStartTime().format(DateTimeFormat.INPUT_TIME.getFormatter());
        String savedTime2 = event.getEndTime() == null
                ? ""
                : event.getEndTime().format(DateTimeFormat.INPUT_TIME.getFormatter());
        return "E | " + status + " | " + task.getDescription()
                + " | " + event.getStartDate() + " | " + savedTime1
                + " | " + event.getEndDate() + " | " + savedTime2;
    }
}
