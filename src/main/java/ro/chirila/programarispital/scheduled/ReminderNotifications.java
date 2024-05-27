package ro.chirila.programarispital.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.service.AppointmentService;
import ro.chirila.programarispital.service.SendEmailService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ReminderNotifications {
    public final SendEmailService sendEmailService;
    public final AppointmentService appointmentService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReminderNotifications(SendEmailService sendEmailService, AppointmentService appointmentService) {
        this.sendEmailService = sendEmailService;
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "0 10 * * *")
    public void sendReminderNotifications() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        LocalDate today = LocalDate.now();
        for (AppointmentResponseDTO appointment : appointments) {
            LocalDate appointmentDate = LocalDate.parse(appointment.getChooseDate(), formatter);

            long daysBetween = ChronoUnit.DAYS.between(today, appointmentDate);

            if (daysBetween == 1) {
                sendEmailService.sendAppointmentEmail(appointment);
            }
        }
    }
}
