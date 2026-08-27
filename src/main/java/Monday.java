import java.util.ArrayList;
import java.util.Scanner;

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
        String banner = "____________________________________________________________\n"
                + " __  __   ___   _   _  ____      _    __   __\n"
                + "|  \\/  | / _ \\ | \\ | ||  _ \\    / \\   \\ \\ / /\n"
                + "| |\\/| || | | ||  \\| || | | |  / _ \\   \\ V / \n"
                + "| |  | || |_| || |\\  || |_| | / ___ \\   | |  \n"
                + "|_|  |_| \\___/ |_| \\_||____/ /_/   \\_\\  |_|  \n"
                + "Hello! My name is Monday.\n"
                + "How can I help you today?\n"
                + "____________________________________________________________";
        System.out.println(banner);

        ArrayList<Task> list = new ArrayList<>();
        String line = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String command = scanner.nextLine();

                System.out.println(line);

                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                }

                if (command.equals("list")) {
                    showList(list, line);
                    continue;
                }

                if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(command, list, line);
                    continue;
                }

                if (command.equals("unmark") || command.startsWith("unmark ")) {
                    unmarkTask(command, list, line);
                    continue;
                }

                if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command, list, line);
                    continue;
                }

                if (command.equals("todo") || command.startsWith("todo ")) {
                    addTodo(command, list, line);
                    continue;
                }

                if (command.equals("deadline") || command.startsWith("deadline ")) {
                    deadlineTask(command, list, line);
                    continue;
                }

                if (command.equals("event") || command.startsWith("event ")) {
                    eventTask(command, list, line);
                    continue;
                }

                throw new MondayException("Please tell me your task or which task to mark/unmark.");

            } catch (MondayException e) {
                System.out.println(e.getMessage());
                System.out.println(line);
            }
        }
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param list the list of tasks to display
     * @param line the separator line printed after the task list
     */
    private static void showList(ArrayList<Task> list, String line) {
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < list.size(); index++) {
            System.out.println((index + 1) + "." + list.get(index));
        }
        System.out.println(line);
    }

    /**
     * Marks the specified task as completed.
     *
     * @param command the user command containing the task number to mark
     * @param list the list of tasks
     * @param line the separator line printed after the response
     * @throws MondayException if the task number is missing, invalid, or out of range
     */
    private static void markTask(String command, ArrayList<Task> list, String line) throws MondayException {
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
        System.out.println("Nice! I've marked this task as done:\n" + "  " + list.get(index));
        System.out.println(line);
    }

    /**
     * Marks the specified task as not completed.
     *
     * @param command the user command containing the task number to unmark
     * @param list the list of tasks
     * @param line the separator line printed after the response
     * @throws MondayException if the task number is missing, invalid, or out of range
     */
    private static void unmarkTask(String command, ArrayList<Task> list, String line) throws MondayException {
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
        System.out.println("OK, I've marked this task as not done yet:\n" + "  " + list.get(index));
        System.out.println(line);
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param command the user command containing the task number to delete
     * @param list the list of tasks
     * @param line the separator line printed after the response
     * @throws MondayException if the task number is missing, invalid, or out of range
     */
    private static void deleteTask(String command, ArrayList<Task> list, String line) throws MondayException {
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
        System.out.println("Noted. I've removed this task:\n" + "  " + deletedTask +
                "\nNow you have " + list.size() + " tasks in the list.");
        System.out.println(line);
    }

    /**
     * Adds a todo task to the task list.
     *
     * @param command the user command containing the todo description
     * @param list the list of tasks
     * @param line the separator line printed after the response
     * @throws MondayException if the todo description is missing
     */
    private static void addTodo(String command, ArrayList<Task> list, String line) throws MondayException {
        String task = command.substring("todo".length()).trim();
        if (task.isEmpty()) {
            throw new MondayException("Please tell me your todo task.");
        }

        list.add(new Todo(task));
        System.out.println("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1));
        System.out.println("Now you have " + list.size() + " tasks in the list.");
        System.out.println(line);
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param command the user command containing the task description and deadline
     * @param list the list of tasks
     * @param line the separator line printed after the response
     * @throws MondayException if the task description, deadline, or required /by keyword is missing
     */
    private static void deadlineTask(String command, ArrayList<Task> list, String line) throws MondayException {
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
        System.out.println("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1));
        System.out.println("Now you have " + list.size() + " tasks in the list.");
        System.out.println(line);
    }

    /**
     * Adds an event task to the task list.
     *
     * @param command the user command containing the event description, start time, and end time
     * @param list the list of tasks
     * @param line the separator line printed after the response
     * @throws MondayException if the event details are missing or the /from and /to keywords are invalid
     */
    private static void eventTask(String command, ArrayList<Task> list, String line) throws MondayException {
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
        System.out.println("Got it. I've added this task:\n" + "  " + list.get(list.size() - 1));
        System.out.println("Now you have " + list.size() + " tasks in the list.");
        System.out.println(line);
    }
}
