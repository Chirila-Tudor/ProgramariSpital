package ro.chirila.programarispital.repository.dto;

import lombok.Data;
import ro.chirila.programarispital.repository.entity.PeriodOfAppointment;

@Data
public class AppointmentUpdateDTO {
    private String chooseDate;
    private String appointmentHour;
    private PeriodOfAppointment periodOfAppointment;
}
