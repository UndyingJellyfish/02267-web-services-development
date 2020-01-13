package exceptions;

public class UsedTokenException extends TokenException {
    private static String defaultErrorString = "Token does not exist.";

    public UsedTokenException(String message) {
        super(message);
    }
    public UsedTokenException(){
        this(defaultErrorString);
    }
}
