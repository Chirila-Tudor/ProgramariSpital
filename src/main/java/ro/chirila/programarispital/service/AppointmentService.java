package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;

import java.util.List;

@Service
public interface AppointmentService {
    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointment, String username);

    AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO);

    void deleteAppointmentById(Long id);

    List<AppointmentResponseDTO> getAllAppointments();

    List<AppointmentResponseDTO> getAllFutureAppointments();

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAppointmentsByScheduledPerson(String username);

    List<AppointmentResponseDTO> getAppointmentsForDoctor(String doctorUsername);
    List<String> getAvailableTimes(String chooseDate, Long idService, String doctorUsername);

    boolean isDoctorAvailableOnDate(String chooseDate, Long idService, String doctorUsername);
    List<String> getPeriodOptions();
}
