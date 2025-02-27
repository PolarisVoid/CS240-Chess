package exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class AlreadyTakenException extends Exception{
    public AlreadyTakenException(String message) {
        super(message);
    }
}
