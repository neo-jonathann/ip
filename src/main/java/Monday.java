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

        String[] list = new String[100];
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
                    System.out.println(j + ". " + list[i]);
                    j++;
                    i++;
                }
                continue;
            }
            list[numberOfTasks] = command;
            numberOfTasks++;
            System.out.println("added: " + command);
            System.out.println(line);
        }
    }
}
