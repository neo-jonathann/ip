/**
 * Signals an invalid command or missing command detail entered by the user.
 */
public class MondayException extends Exception{
    public MondayException(String message) {
        super(message);
    }
}
