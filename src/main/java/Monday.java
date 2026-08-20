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
                while (i < numberOfTasks) {
                    System.out.println(j + "." + list[i]);
                    j++;
                    i++;
                }
                continue;
            }

            if (command.startsWith("mark ")) {
                int index = Integer.parseInt(command.substring(5)) - 1; // extract the number after 'mark' and minus 1 because indices start from 0
                // Do not need this anymore: String temp = list[index].substring(4);
                list[index].markAsDone(); // previously this: list[index] = "[X] " + temp; // replace '[ ]' with '[X]'
                System.out.println("Nice! I've marked this task as done:\n" + list[index]);
                continue;
            }

            if (command.startsWith("unmark ")) {
                int index = Integer.parseInt(command.substring(7)) - 1;
                // Do not need this anymore: String temp = list[index].substring(4);
                list[index].markAsNotDone(); // previously this: list[index] = "[ ] " + temp;
                System.out.println("OK, I've marked this task as not done yet:\n" + list[index]);
                continue;
            }

            list[numberOfTasks] = new Task(command); // previously this: "[ ] " + command;
            numberOfTasks++;
            System.out.println("added: " + command);
            System.out.println(line);
        }
    }
}
