package ro.chirila.programarispital.repository.dto;

import lombok.Data;
import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.TypeOfService;

import java.util.List;
@Data
public class UserResponseDTO
{
    private Long id;
    private String username;
    private Role role;
    private List<Appointment> appointments;
    private List<TypeOfService> services;
    private Boolean isActive;
}
