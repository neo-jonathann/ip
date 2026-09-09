package monday.command;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import monday.exception.MondayException;
import monday.storage.Storage;
import monday.task.Deadline;
import monday.task.Event;
import monday.task.Task;
import monday.task.TaskList;
import monday.task.Todo;
import monday.ui.Ui;
import monday.util.DateTimeFormat;

/**
 * Interprets user commands and applies them to the task list.
 */
public class Parser {
    /**
     * Processes one user command.
     *
     * @param command command entered by the user.
     * @param tasks task list to update or display.
     * @param ui user interface used to display results.
     * @return true if Monday should continue running, false otherwise.
     * @throws MondayException if the command is invalid.
     */
    public boolean executeCommand(String command, TaskList tasks, Ui ui) throws MondayException {
        if (command.equals("bye")) {
            Storage.saveTask(tasks);
            ui.showResponse("Bye. Hope to see you again soon!");
            return false;
        }

        if (command.equals("list")) {
            ui.showTaskList(tasks);
            return true;
        }

        if (command.equals("mark") || command.startsWith("mark ")) {
            markTask(command, tasks, ui);
            return true;
        }

        if (command.equals("unmark") || command.startsWith("unmark ")) {
            unmarkTask(command, tasks, ui);
            return true;
        }

        if (command.equals("delete") || command.startsWith("delete ")) {
            deleteTask(command, tasks, ui);
            return true;
        }

        if (command.equals("todo") || command.startsWith("todo ")) {
            addTodo(command, tasks, ui);
            return true;
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            addDeadline(command, tasks, ui);
            return true;
        }

        if (command.equals("event") || command.startsWith("event ")) {
            addEvent(command, tasks, ui);
            return true;
        }

        if (command.equals("find") || command.startsWith("find ")) {
            findTask(command, tasks, ui);
            return true;
        }

        throw new MondayException("Please tell me your task or which task to mark/unmark.");
    }

    /**
     * Marks a specified task as completed.
     * Users should enter the command in the following format:
     * {@code mark <task number>}
     *
     * @param command command containing the task number.
     * @param tasks task list containing the task.
     * @param ui user interface used to display the result.
     * @throws MondayException if the task number is invalid.
     */
    private void markTask(String command, TaskList tasks, Ui ui) throws MondayException {
        int index = getTaskIndex(command, "mark", tasks,
                "Please tell me which task number to mark.",
                "Please tell me a valid task number to mark.");

        tasks.get(index).markAsDone();
        Storage.saveTask(tasks);
        ui.showResponse("Nice! I've marked this task as done:", "  " + tasks.get(index));
    }

    /**
     * Marks a specified task as not completed.
     * Users should enter the command in the following format:
     * {@code unmark <task number>}
     *
     * @param command command containing the task number.
     * @param tasks task list containing the task.
     * @param ui user interface used to display the result.
     * @throws MondayException if the task number is invalid.
     */
    private void unmarkTask(String command, TaskList tasks, Ui ui) throws MondayException {
        int index = getTaskIndex(command, "unmark", tasks,
                "Please tell me which task number to unmark.",
                "Please tell me a valid task number to unmark.");

        tasks.get(index).markAsNotDone();
        Storage.saveTask(tasks);
        ui.showResponse("OK, I've marked this task as not done yet:", "  " + tasks.get(index));
    }

    /**
     * Removes a specified task from the task list.
     * Users should enter the command in the following format:
     * {@code delete <task number>}
     *
     * @param command command containing the task number.
     * @param tasks task list containing the task.
     * @param ui user interface used to display the result.
     * @throws MondayException if the task number is invalid.
     */
    private void deleteTask(String command, TaskList tasks, Ui ui) throws MondayException {
        int index = getTaskIndex(command, "delete", tasks,
                "Please tell me which task number to delete.",
                "Please tell me a valid task number to delete.");

        Task deletedTask = tasks.remove(index);
        Storage.saveTask(tasks);
        ui.showResponse("Noted. I've removed this task:", "  " + deletedTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Adds a todo task to the task list.
     * Users should enter the command in the following format:
     * {@code todo <description> /notes <notes>}
     *
     * @param command command containing the todo description.
     * @param tasks task list to update.
     * @param ui user interface used to display the result.
     * @throws MondayException if the description is missing.
     */
    private void addTodo(String command, TaskList tasks, Ui ui) throws MondayException {
        String description = command
                .substring(command.indexOf("todo") + "todo".length(), command.indexOf("/notes")).trim();
        String notes = command.substring(command.indexOf("/notes") + "/notes".length()).trim();
        if (description.isEmpty()) {
            throw new MondayException("Please tell me your todo task.");
        }

        tasks.add(new Todo(description, notes));
        Storage.saveTask(tasks);
        showAddedTask(tasks, ui);
    }

    /**
     * Adds a deadline task to the task list.
     * Users should enter the command in the following format:
     * {@code deadline <description> /by <deadline> /notes <notes>}
     *
     * @param command command containing the deadline details.
     * @param tasks task list to update.
     * @param ui user interface used to display the result.
     * @throws MondayException if the deadline details are invalid.
     */
    private void addDeadline(String command, TaskList tasks, Ui ui) throws MondayException {
        if (command.length() == "deadline".length()) {
            throw new MondayException("Please tell me your task.");
        }

        if (!command.contains("/by")) {
            throw new MondayException("Please use the '/by' command.");
        }

        String description = command.substring("deadline ".length(), command.indexOf("/by")).trim();
        if (description.isEmpty()) {
            throw new MondayException("Please tell me your task.");
        }

        String deadline = command
                .substring(command.indexOf("/by") + "/by".length(), command.indexOf("/notes")).trim();
        if (deadline.isEmpty()) {
            throw new MondayException("Please tell me your deadline.");
        }

        String[] dateTimeParts = deadline.split("\\s+");
        if (dateTimeParts.length > 2) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }

        String notes = command.substring(command.indexOf("/notes") + "/notes".length()).trim();

        try {
            LocalDate date = LocalDate.parse(dateTimeParts[0], DateTimeFormat.INPUT_DATE.getFormatter());
            LocalTime time = parseOptionalTime(dateTimeParts);
            tasks.add(new Deadline(description, date, time, notes));
            Storage.saveTask(tasks);
            showAddedTask(tasks, ui);
        } catch (DateTimeParseException e) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }
    }

    /**
     * Adds an event task to the task list.
     * Users should enter the command in the following format:
     * {@code event <description> /from <start time> /to <end time> /notes <notes>}
     *
     * @param command command containing the event details.
     * @param tasks task list to update.
     * @param ui user interface used to display the result.
     * @throws MondayException if the event details are invalid.
     */
    private void addEvent(String command, TaskList tasks, Ui ui) throws MondayException {
        if (command.length() == "event".length()) {
            throw new MondayException("Please tell me your task.");
        }

        if (!command.contains("/from") || !command.contains("/to")) {
            throw new MondayException("Please use the '/from' and '/to' commands.");
        }

        if (command.indexOf("/from") > command.indexOf("/to")) {
            throw new MondayException("The '/from' command needs to come before the '/to' command.");
        }

        String description = command.substring("event ".length(), command.indexOf("/from")).trim();
        if (description.isEmpty()) {
            throw new MondayException("Please tell me your task.");
        }

        String startPeriod = command
                .substring(command.indexOf("/from") + "/from".length(), command.indexOf("/to")).trim();
        if (startPeriod.isEmpty()) {
            throw new MondayException("Please tell me your start time.");
        }

        String endPeriod = command
                .substring(command.indexOf("/to") + "/to".length(), command.indexOf("/notes")).trim();
        if (endPeriod.isEmpty()) {
            throw new MondayException("Please tell me your end time.");
        }

        String[] startParts = startPeriod.split("\\s+");
        String[] endParts = endPeriod.split("\\s+");
        if (startParts.length > 2 || endParts.length > 2) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }

        String notes = command.substring(command.indexOf("/notes") + "/notes".length()).trim();

        try {
            LocalDate startDate = LocalDate.parse(startParts[0], DateTimeFormat.INPUT_DATE.getFormatter());
            LocalTime startTime = parseOptionalTime(startParts);

            LocalDate endDate = LocalDate.parse(endParts[0], DateTimeFormat.INPUT_DATE.getFormatter());
            LocalTime endTime = parseOptionalTime(endParts);

            tasks.add(new Event(description, startDate, startTime, endDate, endTime, notes));
            Storage.saveTask(tasks);
            showAddedTask(tasks, ui);
        } catch (DateTimeParseException e) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }
    }

    /**
     * Finds and displays tasks whose descriptions contain a keyword.
     * Users should enter the command in the following format:
     * {@code find <keyword>}
     *
     * @param command command containing the keyword
     * @param tasks task list to search
     * @param ui user interface used to display the matching tasks
     * @throws MondayException if the keyword is missing
     */
    private void findTask(String command, TaskList tasks, Ui ui) throws MondayException {
        String keyword = command.substring("find".length()).trim();

        if (keyword.isEmpty()) {
            throw new MondayException("Please tell me what to find.");
        }

        TaskList matchingTasks = tasks.find(keyword);
        ui.showMatchingTaskList(matchingTasks);
    }

    /**
     * Returns a zero-based task index from a command containing a task number.
     *
     * @param command command containing the task number.
     * @param commandWord command word at the start of the command.
     * @param tasks task list used to validate the task number.
     * @param missingNumberMessage message displayed when a number is absent.
     * @param invalidNumberMessage message displayed when a number is invalid.
     * @return zero-based index of the requested task.
     * @throws MondayException if the task number is invalid.
     */
    private int getTaskIndex(String command, String commandWord, TaskList tasks,
            String missingNumberMessage, String invalidNumberMessage)
            throws MondayException {
        String numberText = command.substring(commandWord.length()).trim();
        if (numberText.isEmpty()) {
            throw new MondayException(missingNumberMessage);
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new MondayException("Please enter a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new MondayException(invalidNumberMessage);
        }

        return taskNumber - 1;
    }

    /**
     * Parses an optional time from a date-and-time input.
     *
     * @param dateTimeParts date and optional time parts.
     * @return parsed time, or null when no time was supplied.
     */
    private LocalTime parseOptionalTime(String[] dateTimeParts) {
        if (dateTimeParts.length == 1) {
            return null;
        }

        return LocalTime.parse(dateTimeParts[1], DateTimeFormat.INPUT_TIME.getFormatter());
    }

    /**
     * Displays the task most recently added to the task list.
     *
     * @param tasks task list containing the new task.
     * @param ui user interface used to display the result.
     */
    private void showAddedTask(TaskList tasks, Ui ui) {
        assert tasks.size() > 0 : "There are currently no tasks in the list. There must at least be one task.";

        ui.showResponse("Got it. I've added this task:",
                "  " + tasks.get(tasks.size() - 1),
                "Now you have " + tasks.size() + " tasks in the list.");
    }
}
