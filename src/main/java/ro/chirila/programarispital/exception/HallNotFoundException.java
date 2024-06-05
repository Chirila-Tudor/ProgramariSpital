package ro.chirila.programarispital.exception;

public class HallNotFoundException extends RuntimeException{
    public HallNotFoundException(String message) {
        super(message);
    }
}
