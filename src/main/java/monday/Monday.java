package monday;

import monday.command.Parser;
import monday.exception.MondayException;
import monday.storage.Storage;
import monday.task.TaskList;
import monday.ui.Ui;

/**
 * Runs the Monday task-management chatbot.
 */
public class Monday {
    /**
     * Starts the chatbot and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        TaskList tasks = new TaskList(Storage.loadTask());
        Ui ui = new Ui();
        ui.showWelcome();
        Parser parser = new Parser();
        while (true) {
            try {
                String command = ui.readCommand();
                if (!parser.executeCommand(command, tasks, ui)) {
                    break;
                }

            } catch (MondayException e) {
                ui.showResponse(e.getMessage());
            }
        }
    }
}
