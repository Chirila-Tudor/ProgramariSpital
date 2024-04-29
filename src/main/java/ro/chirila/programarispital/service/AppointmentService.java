package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;

@Service
public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointment, String username);

    AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO);

    void deleteAppointmentById(Long id);


}
