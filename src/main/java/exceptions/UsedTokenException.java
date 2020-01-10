package exceptions;

public class UsedTokenException extends TokenException {
    public UsedTokenException(String message) {
        super(message);
    }
    public UsedTokenException(){
        super();
    }
}
