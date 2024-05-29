package ro.chirila.programarispital.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.chirila.programarispital.repository.entity.PeriodOfAppointment;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private String chooseDate;
    private String appointmentHour;
    private PeriodOfAppointment periodOfAppointment;
    private List<TypeOfServiceDTO> typeOfServices;
    private String scheduledPerson;
}
