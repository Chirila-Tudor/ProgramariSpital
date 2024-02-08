package ro.chirila.programarispital.repository.dto;

import ro.chirila.programarispital.repository.entity.PeriodOfAppointment;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record AppointmentUpdateDTO(
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Date dateOfBirth,
        Date chooseDate,
        LocalTime appointmentHour,
        PeriodOfAppointment periodOfAppointment,
        List<TypeOfServiceDTO> typeOfService

) {
}
