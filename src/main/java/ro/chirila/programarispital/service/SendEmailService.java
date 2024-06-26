package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.*;

@Service
public interface SendEmailService {
    void sendAppointmentEmail(AppointmentResponseDTO appointment);

    void sendPasswordEmail(UserExistsDTO userExistsDTO, AppointmentResponseDTO appointment);

    void sendPasswordForHospitalPersonal(UserExistsDTO userExistsDTO, UserRequestDTO userRequestDTO);

    void sendUpdatedAppointmentEmail(AppointmentUpdateDTO appointmentUpdateDTO, Long id);
    void sendForgotPasswordEmail(String email, String securityCode,String username);
    void sendPassword(String email,String username, String password);
}
