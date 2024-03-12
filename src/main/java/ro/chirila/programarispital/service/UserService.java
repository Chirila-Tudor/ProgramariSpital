package ro.chirila.programarispital.service;


import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.ChangePasswordDTO;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;
@Service
public interface UserService {
    UserResponseDTO addUser(String username);
    UserResponseDTO getUserByUsername(String username);
    Boolean deleteUser(String username);
    UserSecurityDTO login(String username, String password);
    Boolean changePassword(ChangePasswordDTO changePasswordDTO);
    Boolean sendWelcomeEmail(String username);
}
