import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    public static void saveTask(ArrayList<Task> tasks) {
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
                task = new Deadline(parts[2].trim(), isDone, parts[3].trim());
                break;
            case "E":
                task = new Event(parts[2].trim(), isDone, parts[3].trim(), parts[4].trim());
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
            return "D | " + status + " | " + task.getDescription()
                    + " | " + deadline.getDeadline();
        }

        Event event = (Event) task;
        return "E | " + status + " | " + task.getDescription()
                + " | " + event.getTimePeriod1()
                + " | " + event.getTimePeriod2();
    }
}
