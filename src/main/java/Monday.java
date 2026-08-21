import java.util.Scanner;

public class Monday {

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

        Task[] list = new Task[100]; // previously this: String[] list = new String[100];
        int numberOfTasks = 0;

        String line = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            System.out.println(line);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            }

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

            if (command.startsWith("mark ")) {
                int index = Integer.parseInt(command.substring(5)) - 1;
                list[index].markAsDone();
                System.out.println("Nice! I've marked this task as done:\n" + "  " + list[index]);
                System.out.println(line);
                continue;
            }

            if (command.startsWith("unmark ")) {
                int index = Integer.parseInt(command.substring(7)) - 1;
                list[index].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:\n" + "  " + list[index]);
                System.out.println(line);
                continue;
            }

            if (command.startsWith("todo ")) {
                String task = command.substring(5);
                list[numberOfTasks] = new Todo(task);
                System.out.println("Got it. I've added this task:\n" + "  " + list[numberOfTasks]);
                numberOfTasks++;
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
                System.out.println(line);
                continue;
            }

            if (command.startsWith("deadline ")) {
                String task = command.substring("deadline ".length(), command.indexOf("/by")).trim();
                String deadline = command.substring(command.indexOf("/by") + "/by".length()).trim();
                list[numberOfTasks] = new Deadline(task, deadline);
                System.out.println("Got it. I've added this task:\n" + "  " + list[numberOfTasks]);
                numberOfTasks++;
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
                System.out.println(line);
                continue;
            }

            if (command.startsWith("event ")) {
                String task = command.substring("event ".length(), command.indexOf("/from")).trim();
                String timePeriod1 = command.substring(command.indexOf("/from") + "/from".length(),  command.indexOf("/to")).trim();
                String timePeriod2 = command.substring(command.indexOf("/to") + "/to".length()).trim();
                list[numberOfTasks] = new Event(task, timePeriod1, timePeriod2);
                System.out.println("Got it. I've added this task:\n" +  "  " + list[numberOfTasks]);
                numberOfTasks++;
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
                System.out.println(line);
                continue;
            }

            list[numberOfTasks] = new Task(command);
            numberOfTasks++;
            System.out.println("added: " + command);
            System.out.println(line);
        }
    }
}
