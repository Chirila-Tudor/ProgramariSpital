package ro.chirila.programarispital.service;

import ro.chirila.programarispital.repository.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO addUser(String username);
}
