package ro.chirila.programarispital.repository.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ro.chirila.programarispital.repository.entity.PeriodOfAppointment;

import java.util.List;
@Data
public class AppointmentRequestDTO
{
    @NotBlank(message = "Please enter a valid email.") private String email;
    @NotBlank(message = "Please enter the first-name.") private String firstName;
    @NotBlank(message = "Please enter the last-name.") private String lastName;
    @NotBlank(message = "Please enter a phone number.") private String phoneNumber;
    @NotBlank(message = "Please choose a date of birth.") private String dateOfBirth;
    @NotBlank(message = "Please choose the date of the appointment.") private String chooseDate;
    @NotBlank(message = "Please choose the appointment hour.") private String appointmentHour;
    @NotBlank(message = "Please choose the period of time.") private PeriodOfAppointment periodOfAppointment;
    @NotBlank(message = "Please choose the type of service.") private List<TypeOfServiceDTO> typeOfServices;
}
