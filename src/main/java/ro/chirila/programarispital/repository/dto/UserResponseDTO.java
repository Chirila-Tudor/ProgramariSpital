package ro.chirila.programarispital.repository.dto;

import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.TypeOfService;

import java.util.List;

public record UserResponseDTO(
        String username,
        Role role,
        List<Appointment> appointments,
        List<TypeOfService> services,
        Boolean isActive) {

}
