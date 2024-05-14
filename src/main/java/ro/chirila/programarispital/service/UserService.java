package ro.chirila.programarispital.service;


import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.ChangePasswordDTO;
import ro.chirila.programarispital.repository.dto.UserExistsDTO;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;

@Service
public interface UserService {
    UserResponseDTO addUser(String username);
    UserResponseDTO getUserByUsername(String username);
    Boolean deleteUser(String username);
    UserSecurityDTO login(String username, String password);
    UserExistsDTO getUserExistByUsername(String username);
    boolean changePassword(ChangePasswordDTO changePasswordDTO);
}
