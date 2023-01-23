package pl.edu.umg.linkumg.exceptions;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String message) {
        super(message);
    }
}
