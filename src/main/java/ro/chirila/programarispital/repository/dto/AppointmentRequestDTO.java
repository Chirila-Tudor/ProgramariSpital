package ro.chirila.programarispital.repository.dto;

import jakarta.validation.constraints.NotBlank;
import ro.chirila.programarispital.repository.entity.PeriodOfAppointment;
import ro.chirila.programarispital.repository.entity.User;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record AppointmentRequestDTO(
        @NotBlank(message = "Please enter a valid email.") String email,
        @NotBlank(message = "Please enter the first-name.") String firstName,
        @NotBlank(message = "Please enter the last-name.") String lastName,
        @NotBlank(message = "Please enter a phone number.") String phoneNumber,
        @NotBlank(message = "Please choose a date of birth.") Date dateOfBirth,
        @NotBlank(message = "Please choose the date of the appointment.") Date chooseDate,
        @NotBlank(message = "Please choose the appointment hour.") LocalTime appointmentHour,
        @NotBlank(message = "Please choose the period of time.") PeriodOfAppointment periodOfAppointment,
        @NotBlank(message = "Please choose the type of service.") List<TypeOfServiceDTO> typeOfServices,
        User scheduledPerson
) {
}
