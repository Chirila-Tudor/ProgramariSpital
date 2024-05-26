package ro.chirila.programarispital.repository.dto;

import lombok.Data;
import ro.chirila.programarispital.repository.entity.Role;

@Data
public class UserRequestDTO {
    private String username;
    private Role role;
    private String email;
}
