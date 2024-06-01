package ro.chirila.programarispital.service;


import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.ChangePasswordDTO;
import ro.chirila.programarispital.repository.dto.UserExistsDTO;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;
import ro.chirila.programarispital.repository.entity.Role;

import java.util.List;

@Service
public interface UserService {
    UserResponseDTO addUser(String username, Role role, String email);

    UserResponseDTO getUserByUsername(String username);

    Boolean deleteUser(String username);

    UserSecurityDTO login(String username, String password);

    UserExistsDTO setPasswordForPatient(String username);

    UserExistsDTO setPasswordForUser(String username, Role role);

    Boolean changePassword(ChangePasswordDTO changePasswordDTO);

    String forgotPassword(String username);

    String requestNewPassword(String username, String securityCode);

    Boolean isFirstLogin(String username);

    List<UserResponseDTO> getAllUsersForAdmin();

    Boolean modifyUserActivity(Long id);
    String getEmailByUsername(String username);
}
