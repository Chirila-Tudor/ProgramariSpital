package ro.chirila.programarispital.service.implementation;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.BadCredentialsException;
import ro.chirila.programarispital.exception.UserAlreadyDeactivatedException;
import ro.chirila.programarispital.exception.UserAlreadyExistException;
import ro.chirila.programarispital.exception.UserNotFoundException;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${app.bcrypt.salt}")
    private String bcryptSalt;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDTO addUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistException("User already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setHasPassword(false);
        user.setActive(false);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public Boolean deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (user.getActive()) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public UserSecurityDTO login(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getActive() && password.equals(user.getPassword())) {
                return modelMapper.map(user, UserSecurityDTO.class);
            }
            if (!user.getActive() && password.equals(user.getPassword())) {
                throw new UserAlreadyDeactivatedException("User was deactivated");
            }

        }
        throw new BadCredentialsException("Bad credentials.");
    }


}
