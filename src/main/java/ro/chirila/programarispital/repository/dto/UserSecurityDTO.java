package ro.chirila.programarispital.repository.dto;

import lombok.Data;

@Data
public class UserSecurityDTO
{
    private String username;
    private String role;
    private Boolean isFirstLogin;
//    private String securityCode;
//    private String email;
}
