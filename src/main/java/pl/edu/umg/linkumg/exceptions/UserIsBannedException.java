package pl.edu.umg.linkumg.exceptions;

public class UserIsBannedException extends Exception{
    public UserIsBannedException(String message) {
        super(message);
    }
}
