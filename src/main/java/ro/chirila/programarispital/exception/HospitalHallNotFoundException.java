package ro.chirila.programarispital.exception;

public class HospitalHallNotFoundException extends RuntimeException{
    public HospitalHallNotFoundException(String message) {
        super(message);
    }
}
