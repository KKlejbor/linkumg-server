package pl.edu.umg.linkumg.exception;

public class UserIsBannedException extends Exception{
    public UserIsBannedException(String message) {
        super(message);
    }
}
