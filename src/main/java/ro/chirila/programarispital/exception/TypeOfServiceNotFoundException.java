package ro.chirila.programarispital.exception;

public class TypeOfServiceNotFoundException extends RuntimeException{
    public TypeOfServiceNotFoundException(String message) {
        super(message);
    }
}
