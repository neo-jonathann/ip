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
        String banner =
                "____________________________________________________________\n"
                + " __  __   ___   _   _  ____      _    __   __\n"
                + "|  \\/  | / _ \\ | \\ | ||  _ \\    / \\   \\ \\ / /\n"
                + "| |\\/| || | | ||  \\| || | | |  / _ \\   \\ V / \n"
                + "| |  | || |_| || |\\  || |_| | / ___ \\   | |  \n"
                + "|_|  |_| \\___/ |_| \\_||____/ /_/   \\_\\  |_|  \n"
                + "Hello! My name is Monday.\n"
                + "How can I help you today?\n"
                + "____________________________________________________________";
        System.out.println(banner);

        Task[] list = new Task[100];
        int numberOfTasks = 0;

        String line = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String command = scanner.nextLine();

                System.out.println(line);

                /*
                This is the 'bye' command block.
                */
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                }

                /*
                This is the 'list' command block.
                */
                if (command.equals("list")) {
                    int i = 0;
                    int j = 1;
                    System.out.println("Here are the tasks in your list:");
                    while (i < numberOfTasks) {
                        System.out.println(j + "." + list[i]);
                        j++;
                        i++;
                    }
                    System.out.println(line);
                    continue;
                }

                /*
                This is the 'mark' command block.
                */
                if (command.equals("mark") || command.startsWith("mark ")) {
                    if (command.length() == "mark".length()) {
                        throw new MondayException("Please tell me what to mark.");
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

                    if (taskNumber < 1 || taskNumber > numberOfTasks) {
                        throw new MondayException("Please tell me a valid task number to mark.");
                    }

                    int index = taskNumber - 1;
                    list[index].markAsDone();
                    System.out.println("Nice! I've marked this task as done:\n" + "  " + list[index]);
                    System.out.println(line);
                    continue;
                }

                /*
                This is the 'unmark' command block.
                */
                if (command.equals("unmark") || command.startsWith("unmark ")) {
                    if (command.length() == "unmark".length()) {
                        throw new MondayException("Please tell me what to unmark.");
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

                    if (taskNumber < 1 || taskNumber > numberOfTasks) {
                        throw new MondayException("Please tell me a valid task number to unmark.");
                    }

                    int index = taskNumber - 1;
                    list[index].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:\n" + "  " + list[index]);
                    System.out.println(line);
                    continue;
                }

                /*
                This is the 'todo' command block.
                */
                if (command.equals("todo") || command.startsWith("todo ")) {
                    String task = command.substring("todo".length()).trim();
                    if (task.isEmpty()) {
                        throw new MondayException("Please tell me your todo task.");
                    }

                    list[numberOfTasks] = new Todo(task);
                    System.out.println("Got it. I've added this task:\n" + "  " + list[numberOfTasks]);
                    numberOfTasks++;
                    System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
                    System.out.println(line);
                    continue;
                }

                /*
                This is the 'deadline' command block.
                */
                if (command.equals("deadline") || command.startsWith("deadline ")) {
                    if (command.length() == "deadline".length()) { // The user input "deadline" only.
                        throw new MondayException("Please tell me your task.");
                    }

                    if (!command.contains("/by")) {
                        throw new MondayException("Please use the '/by' command."); // The user did not use '/by'.
                    }

                    String task = command.substring("deadline ".length(), command.indexOf("/by")).trim();
                    if (task.isEmpty()) {
                        throw new MondayException("Please tell me your task.");
                    }

                    String deadline = command.substring(command.indexOf("/by") + "/by".length()).trim();
                    if (deadline.isEmpty()) {
                        throw new MondayException("Please tell me your deadline task.");
                    }

                    list[numberOfTasks] = new Deadline(task, deadline);
                    System.out.println("Got it. I've added this task:\n" + "  " + list[numberOfTasks]);
                    numberOfTasks++;
                    System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
                    System.out.println(line);
                    continue;
                }

                /*
                This is the 'event' command block.
                */
                if (command.equals("event") || command.startsWith("event ")) {
                    if (command.length() == "event".length()) { // The user input "event" only.
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

                    String timePeriod1 = command.substring(command.indexOf("/from") + "/from".length(), command.indexOf("/to")).trim();
                    if (timePeriod1.isEmpty()) {
                        throw new MondayException("Please tell me your start time.");
                    }

                    String timePeriod2 = command.substring(command.indexOf("/to") + "/to".length()).trim();
                    if (timePeriod2.isEmpty()) {
                        throw new MondayException("Please tell me your end time.");
                    }

                    list[numberOfTasks] = new Event(task, timePeriod1, timePeriod2);
                    System.out.println("Got it. I've added this task:\n" + "  " + list[numberOfTasks]);
                    numberOfTasks++;
                    System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
                    System.out.println(line);
                    continue;
                }

                throw new MondayException("Please tell me your task or which task to mark/unmark.");

            } catch (MondayException e) {
                System.out.println(e.getMessage());
                System.out.println(line);
            }
        }
    }
}
