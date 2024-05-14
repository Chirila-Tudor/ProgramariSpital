package ro.chirila.programarispital.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.chirila.programarispital.repository.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExistsDTO {
    private String username;
    private String password;
    private Boolean isActive;
    private Boolean hasPassword;
    private Role role;
    private Boolean isFirstLogin;

}
