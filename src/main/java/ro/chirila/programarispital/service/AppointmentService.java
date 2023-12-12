package ro.chirila.programarispital.service;

import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;

public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointment, String username);

    AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO);

    void deleteAppointmentById(Long id);
}
