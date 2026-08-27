import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Monday task-management chatbot.
 */
public class Monday {
    private static final String LINE = "____________________________________________________________";

    /**
     * Starts the chatbot and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = LINE + "\n"
                + " __  __   ___   _   _  ____      _    __   __\n"
                + "|  \\/  | / _ \\ | \\ | ||  _ \\    / \\   \\ \\ / /\n"
                + "| |\\/| || | | ||  \\| || | | |  / _ \\   \\ V / \n"
                + "| |  | || |_| || |\\  || |_| | / ___ \\   | |  \n"
                + "|_|  |_| \\___/ |_| \\_||____/ /_/   \\_\\  |_|  \n"
                + "Hello! My name is Monday.\n"
                + "How can I help you today?\n"
                + LINE;
        System.out.println(banner);

        ArrayList<Task> list = Storage.loadTask();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String command = scanner.nextLine();

                if (command.equals("bye")) {
                    Storage.saveTask(list);
                    printResponse("Bye. Hope to see you again soon!");
                    break;
                }

                if (command.equals("list")) {
                    showList(list);
                    continue;
                }

                if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(command, list);
                    continue;
                }

                if (command.equals("unmark") || command.startsWith("unmark ")) {
                    unmarkTask(command, list);
                    continue;
                }

                if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command, list);
                    continue;
                }

                if (command.equals("todo") || command.startsWith("todo ")) {
                    addTodo(command, list);
                    continue;
                }

                if (command.equals("deadline") || command.startsWith("deadline ")) {
                    deadlineTask(command, list);
                    continue;
                }

                if (command.equals("event") || command.startsWith("event ")) {
                    eventTask(command, list);
                    continue;
                }

                throw new MondayException("Please tell me your task or which task to mark/unmark.");

            } catch (MondayException e) {
                printResponse(e.getMessage());
            }
        }
    }

    /**
     * Prints a response between separator lines.
     *
     * @param response the response to display
     */
    private static void printResponse(String response) {
        System.out.println(LINE);
        System.out.println(response);
        System.out.println(LINE);
    }

    /**
     * Displays all tasks currently in the task list.
     * Users should enter the command in the following format:
     * {@code list}
     *
     * @param list the list of tasks to display
     */
    private static void showList(ArrayList<Task> list) {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int index = 0; index < list.size(); index++) {
            response.append("\n").append(index + 1).append(".").append(list.get(index));
        }
        printResponse(response.toString());
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
    private static void markTask(String command, ArrayList<Task> list) throws MondayException {
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
        printResponse("Nice! I've marked this task as done:\n" + "  " + list.get(index));
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
    private static void unmarkTask(String command, ArrayList<Task> list) throws MondayException {
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
        printResponse("OK, I've marked this task as not done yet:\n" + "  " + list.get(index));
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
    private static void deleteTask(String command, ArrayList<Task> list) throws MondayException {
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
        printResponse("Noted. I've removed this task:\n" + "  " + deletedTask +
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
    private static void addTodo(String command, ArrayList<Task> list) throws MondayException {
        String task = command.substring("todo".length()).trim();
        if (task.isEmpty()) {
            throw new MondayException("Please tell me your todo task.");
        }

        list.add(new Todo(task));
        Storage.saveTask(list);
        printResponse("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1)
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
    private static void deadlineTask(String command, ArrayList<Task> list) throws MondayException {
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
            throw new MondayException("Please tell me your deadline task.");
        }

        list.add(new Deadline(task, deadline));
        Storage.saveTask(list);
        printResponse("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1)
                      + "\nNow you have " + list.size() + " tasks in the list.");
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
    private static void eventTask(String command, ArrayList<Task> list) throws MondayException {
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

        String timePeriod1 = command
                .substring(command.indexOf("/from") + "/from".length(), command.indexOf("/to")).trim();
        if (timePeriod1.isEmpty()) {
            throw new MondayException("Please tell me your start time.");
        }

        String timePeriod2 = command.substring(command.indexOf("/to") + "/to".length()).trim();
        if (timePeriod2.isEmpty()) {
            throw new MondayException("Please tell me your end time.");
        }

        list.add(new Event(task, timePeriod1, timePeriod2));
        Storage.saveTask(list);
        printResponse("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1)
                      + "\nNow you have " + list.size() + " tasks in the list.");
    }
}
