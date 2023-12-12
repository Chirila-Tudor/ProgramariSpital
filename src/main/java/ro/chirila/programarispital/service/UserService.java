package ro.chirila.programarispital.service;

import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;

public interface UserService {
    UserResponseDTO addUser(String username);
    UserResponseDTO getUserByUsername(String username);
    Boolean deleteUser(String username);
    UserSecurityDTO login(String username, String password);
}
