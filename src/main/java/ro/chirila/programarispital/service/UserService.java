package ro.chirila.programarispital.service;


import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.*;

import java.util.List;

@Service
public interface UserService {
    UserResponseDTO addUser(String username);
    UserResponseDTO getUserByUsername(String username);
    Boolean deleteUser(String username);
    UserSecurityDTO login(String username, String password);
    UserExistsDTO getUserExistByUsername(String username);
    Boolean changePassword(ChangePasswordDTO changePasswordDTO);
    Boolean forgotPassword(String username);
    Boolean requestNewPassword(String username, String securityCode);
    Boolean isFirstLogin(String username);
    List<UserResponseDTO> getAllUsersForAdmin();
    Boolean modifyUserActivity(Long id);
}
