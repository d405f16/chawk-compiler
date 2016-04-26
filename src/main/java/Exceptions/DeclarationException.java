package Exceptions;

/**
 * Created by Timian on 26-04-2016.
 */
public class DeclarationException extends RuntimeException {
    public DeclarationException() { super(); }
    public DeclarationException(String message) { super(message); }
    public DeclarationException(String message, Throwable cause) { super(message, cause); }
    public DeclarationException(Throwable cause) { super(cause); }
}
