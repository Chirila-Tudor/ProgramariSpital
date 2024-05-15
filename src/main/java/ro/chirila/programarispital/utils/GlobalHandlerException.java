package ro.chirila.programarispital.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.chirila.programarispital.exception.AppointmentNotFoundException;

@ControllerAdvice
@Log4j2
public class GlobalHandlerException {
    @ExceptionHandler(value = AppointmentNotFoundException.class)
    public ResponseEntity<Object> appointmentNotFoundException(AppointmentNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
