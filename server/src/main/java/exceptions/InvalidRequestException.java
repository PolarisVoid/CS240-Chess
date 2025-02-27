package exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class InvalidRequestException extends Exception{
    public InvalidRequestException(String message) {
        super(message);
    }
}
