package ro.chirila.programarispital.repository.dto;

import lombok.Data;
import ro.chirila.programarispital.repository.entity.Role;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private List<AppointmentResponseDTO> appointments;
    private List<TypeOfServiceDTO> services;
    private Boolean isActive;
}
