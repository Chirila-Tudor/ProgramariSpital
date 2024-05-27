package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.*;

@Service
public interface SendEmailService {
    void sendAppointmentEmail(AppointmentResponseDTO appointment);

    void sendPasswordEmail(UserExistsDTO userExistsDTO, AppointmentResponseDTO appointment);

    void sendPasswordForHospitalPersonal(UserExistsDTO userExistsDTO, UserRequestDTO userRequestDTO);

    void sendForgotPasswordEmail(UserSecurityDTO userSecurityDTO);
}
