package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.UserExistsDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;

@Service
public interface SendEmailService {
    void sendAppointmentEmail(AppointmentResponseDTO appointment);
    void sendPasswordEmail(UserExistsDTO userExistsDTO, AppointmentResponseDTO appointment);
    void sendForgotPasswordEmail(UserSecurityDTO userSecurityDTO);
}
