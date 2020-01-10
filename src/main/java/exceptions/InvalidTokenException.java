package exceptions;

public class InvalidTokenException extends TokenException{

    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException(){
        super();
    }
}
