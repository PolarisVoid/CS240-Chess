package exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class UnathorizedException extends Exception{
    public UnathorizedException(String message) {
        super(message);
    }
}
