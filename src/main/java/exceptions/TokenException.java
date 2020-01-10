package exceptions;

public abstract class TokenException extends Exception {
    public TokenException(String message){
        super(message);
    }
    public TokenException(){
        super();
    }
}

