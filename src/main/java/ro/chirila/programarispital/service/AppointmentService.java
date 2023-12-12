package ro.chirila.programarispital.service;

import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;

public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointment, String username);
    void deleteAppointmentById(Long id);
}
