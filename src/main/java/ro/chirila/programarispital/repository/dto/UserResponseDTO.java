package ro.chirila.programarispital.repository.dto;

import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.TypeOfService;

import java.util.List;

public class UserResponseDTO {
    private String username;
    private Role role;
    private List<Appointment> appointments;
    private List<TypeOfService> services;
}
