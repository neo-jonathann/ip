import java.util.ArrayList;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Runs the Monday task-management chatbot.
 */
public class Monday {
    /**
     * Starts the chatbot and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ArrayList<Task> list = Storage.loadTask();
        Ui ui = new Ui();
        ui.showWelcome();
        while (true) {
            try {
                String command = ui.readCommand();

                if (command.equals("bye")) {
                    Storage.saveTask(list);
                    ui.showResponse("Bye. Hope to see you again soon!");
                    break;
                }

                if (command.equals("list")) {
                    ui.showTaskList(list);
                    continue;
                }

                if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(command, list, ui);
                    continue;
                }

                if (command.equals("unmark") || command.startsWith("unmark ")) {
                    unmarkTask(command, list, ui);
                    continue;
                }

                if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command, list, ui);
                    continue;
                }

                if (command.equals("todo") || command.startsWith("todo ")) {
                    addTodo(command, list, ui);
                    continue;
                }

                if (command.equals("deadline") || command.startsWith("deadline ")) {
                    deadlineTask(command, list, ui);
                    continue;
                }

                if (command.equals("event") || command.startsWith("event ")) {
                    eventTask(command, list, ui);
                    continue;
                }

                throw new MondayException("Please tell me your task or which task to mark/unmark.");

            } catch (MondayException e) {
                ui.showResponse(e.getMessage());
            }
        }
    }

    /**
     * Marks the specified task as completed.
     * Users should enter the command in the following format:
     * {@code mark <task number>}
     *
     * @param command the user command containing the task number to mark
     * @param list the list of tasks
     * @throws MondayException if the task number is missing, invalid, or out of range
     */
    private static void markTask(String command, ArrayList<Task> list, Ui ui) throws MondayException {
        if (command.length() == "mark".length()) {
            throw new MondayException("Please tell me which task number to mark.");
        }

        String indexString = command.substring(4).trim();
        if (indexString.isEmpty()) {
            throw new MondayException("Please tell me which task number to mark.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new MondayException("Please enter a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > list.size()) {
            throw new MondayException("Please tell me a valid task number to mark.");
        }

        int index = taskNumber - 1;
        list.get(index).markAsDone();
        Storage.saveTask(list);
        ui.showResponse("Nice! I've marked this task as done:\n" + "  " + list.get(index));
    }

    /**
     * Marks the specified task as not completed.
     * Users should enter the command in the following format:
     * {@code unmark <task number>}
     *
     * @param command the user command containing the task number to unmark
     * @param list the list of tasks
     * @throws MondayException if the task number is missing, invalid, or out of range
     */
    private static void unmarkTask(String command, ArrayList<Task> list, Ui ui) throws MondayException {
        if (command.length() == "unmark".length()) {
            throw new MondayException("Please tell me which task number to unmark.");
        }

        String indexString = command.substring(6).trim();
        if (indexString.isEmpty()) {
            throw new MondayException("Please tell me which task number to unmark.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new MondayException("Please enter a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > list.size()) {
            throw new MondayException("Please tell me a valid task number to unmark.");
        }

        int index = taskNumber - 1;
        list.get(index).markAsNotDone();
        Storage.saveTask(list);
        ui.showResponse("OK, I've marked this task as not done yet:\n" + "  " + list.get(index));
    }

    /**
     * Deletes the specified task from the task list.
     * Users should enter the command in the following format:
     * {@code delete <task number>}
     *
     * @param command the user command containing the task number to delete
     * @param list the list of tasks
     * @throws MondayException if the task number is missing, invalid, or out of range
     */
    private static void deleteTask(String command, ArrayList<Task> list, Ui ui) throws MondayException {
        if (command.length() == "delete".length()) {
            throw new MondayException("Please tell me which task number to delete.");
        }

        String indexString = command.substring(7).trim();
        if (indexString.isEmpty()) {
            throw new MondayException("Please tell me which task number to delete.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new MondayException("Please enter a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > list.size()) {
            throw new MondayException("Please tell me a valid task number to delete.");
        }

        int index = taskNumber - 1;
        Task deletedTask = list.get(index);
        list.remove(index);
        Storage.saveTask(list);
        ui.showResponse("Noted. I've removed this task:\n" + "  " + deletedTask +
                "\nNow you have " + list.size() + " tasks in the list.");
    }

    /**
     * Adds a todo task to the task list.
     * Users should enter the command in the following format:
     * {@code todo <description>}
     *
     * @param command the user command containing the todo description
     * @param list the list of tasks
     * @throws MondayException if the todo description is missing
     */
    private static void addTodo(String command, ArrayList<Task> list, Ui ui) throws MondayException {
        String task = command.substring("todo".length()).trim();
        if (task.isEmpty()) {
            throw new MondayException("Please tell me your todo task.");
        }

        list.add(new Todo(task));
        Storage.saveTask(list);
        ui.showResponse("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1)
                      + "\nNow you have " + list.size() + " tasks in the list.");
    }

    /**
     * Adds a deadline task to the task list.
     * Users should enter the command in the following format:
     * {@code deadline <description> /by <deadline>}
     *
     * @param command the user command containing the task description and deadline
     * @param list the list of tasks
     * @throws MondayException if the task description, deadline, or required /by keyword is missing
     */
    private static void deadlineTask(String command, ArrayList<Task> list, Ui ui) throws MondayException {
        if (command.length() == "deadline".length()) {
            throw new MondayException("Please tell me your task.");
        }

        if (!command.contains("/by")) {
            throw new MondayException("Please use the '/by' command.");
        }

        String task = command.substring("deadline ".length(), command.indexOf("/by")).trim();
        if (task.isEmpty()) {
            throw new MondayException("Please tell me your task.");
        }

        String deadline = command.substring(command.indexOf("/by") + "/by".length()).trim();
        if (deadline.isEmpty()) {
            throw new MondayException("Please tell me your deadline.");
        }

        String[] dateTimeParts = deadline.split("\\s+");
        if (dateTimeParts.length > 2) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }

        try {
            LocalDate deadlineDate = LocalDate.parse(dateTimeParts[0], DateTimeFormat.INPUT_DATE.getFormatter());
            LocalTime deadlineTime = null;
            if (dateTimeParts.length == 2) {
                deadlineTime = LocalTime.parse(dateTimeParts[1], DateTimeFormat.INPUT_TIME.getFormatter());
            }
            list.add(new Deadline(task, deadlineDate, deadlineTime));
            Storage.saveTask(list);
            ui.showResponse("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1)
                    + "\nNow you have " + list.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }
    }

    /**
     * Adds an event task to the task list.
     * Users should enter the command in the following format:
     * {@code event <description> /from <start time> /to <end time>}
     *
     * @param command the user command containing the event description, start time, and end time
     * @param list the list of tasks
     * @throws MondayException if the event details are missing or the /from and /to keywords are invalid
     */
    private static void eventTask(String command, ArrayList<Task> list, Ui ui) throws MondayException {
        if (command.length() == "event".length()) {
            throw new MondayException("Please tell me your task.");
        }

        if (!command.contains("/from") || !command.contains("/to")) {
            throw new MondayException("Please use the '/from' and '/to' commands.");
        }

        if (command.indexOf("/from") > command.indexOf("/to")) {
            throw new MondayException("The '/from' command needs to come before the '/to' command.");
        }

        String task = command.substring("event ".length(), command.indexOf("/from")).trim();
        if (task.isEmpty()) {
            throw new MondayException("Please tell me your task.");
        }

        String startPeriod1 = command
                .substring(command.indexOf("/from") + "/from".length(), command.indexOf("/to")).trim();
        if (startPeriod1.isEmpty()) {
            throw new MondayException("Please tell me your start time.");
        }

        String endPeriod2 = command.substring(command.indexOf("/to") + "/to".length()).trim();
        if (endPeriod2.isEmpty()) {
            throw new MondayException("Please tell me your end time.");
        }

        String[] startDateTimeParts = startPeriod1.split("\\s+");
        String[] endDateTimeParts = endPeriod2.split("\\s+");

        if (startDateTimeParts.length > 2 || endDateTimeParts.length > 2) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }

        try {
            LocalDate startDate = LocalDate.parse(
                    startDateTimeParts[0], DateTimeFormat.INPUT_DATE.getFormatter());
            LocalTime startTime = null;

            if (startDateTimeParts.length == 2) {
                startTime = LocalTime.parse(
                        startDateTimeParts[1], DateTimeFormat.INPUT_TIME.getFormatter());
            }

            LocalDate endDate = LocalDate.parse(
                    endDateTimeParts[0], DateTimeFormat.INPUT_DATE.getFormatter());
            LocalTime endTime = null;

            if (endDateTimeParts.length == 2) {
                endTime = LocalTime.parse(
                        endDateTimeParts[1], DateTimeFormat.INPUT_TIME.getFormatter());
            }

            list.add(new Event(task, startDate, startTime, endDate, endTime));
            Storage.saveTask(list);
            ui.showResponse("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1)
                    + "\nNow you have " + list.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new MondayException("Please use the format dd/MM/yyyy or dd/MM/yyyy HHmm.");
        }
    }
}
