package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
@Service
public interface SendEmailService {
    void sendAppointmentEmail(AppointmentResponseDTO appointment);
}
