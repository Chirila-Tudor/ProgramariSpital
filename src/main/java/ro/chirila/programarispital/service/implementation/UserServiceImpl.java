package ro.chirila.programarispital.service.implementation;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.BadCredentialsException;
import ro.chirila.programarispital.exception.UserAlreadyDeactivatedException;
import ro.chirila.programarispital.exception.UserAlreadyExistException;
import ro.chirila.programarispital.exception.UserNotFoundException;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.*;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ro.chirila.programarispital.utils.PasswordGenerator.*;

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
    public UserResponseDTO addUser(String username, Role role, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistException("User already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setHasPassword(false);
        user.setIsActive(false);
        user.setRole(role);
        user.setEmail(email);
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
        if (user.getIsActive()) {
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
            // TODO remove hashPassword after adding hashing in UI
            if (user.getIsActive() && hashPassword(password).equals(user.getPassword())) {
                return modelMapper.map(user, UserSecurityDTO.class);
            }
            if (!user.getIsActive() && password.equals(user.getPassword())) {
                throw new UserAlreadyDeactivatedException("User was deactivated");
            }

        }
        throw new BadCredentialsException("Bad credentials.");
    }

    @Override
    public UserExistsDTO setPasswordForPatient(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        UserExistsDTO newUser = null;
        String password = "";
        if (user.isEmpty()) {
            password = generatePassword(12);
            newUser = new UserExistsDTO(username, hashPassword(password), true, true, Role.PATIENT, true);
            userRepository.save(modelMapper.map(newUser, User.class));
            newUser.setPassword(password);
        }
        return newUser;
    }

    @Override
    public UserExistsDTO setPasswordForUser(String username, Role role) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        String password = generatePassword(12);
        user.setPassword(hashPassword(password));
        user.setHasPassword(true);
        user.setIsFirstLogin(true);
        user.setIsActive(true);
        userRepository.save(user);

        UserExistsDTO userExistsDTO = modelMapper.map(user, UserExistsDTO.class);
        userExistsDTO.setPassword(password);

        return userExistsDTO;
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO changePasswordDTO) {

        String username = changePasswordDTO.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        String hashFrontEndPassword = hashPassword(changePasswordDTO.getNewPassword());

        if (!verifyPassword(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(hashFrontEndPassword);
        user.setIsFirstLogin(false);
        userRepository.save(user);
        return true;

    }

    @Override
    public Boolean forgotPassword(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            int length = 10;
            user.setSecurityCode(generateSecurityCode(length));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean requestNewPassword(String username, String securityCode) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getSecurityCode().equals(securityCode);
        }
        return false;
    }

    @Override
    public Boolean isFirstLogin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user.getIsFirstLogin();
    }

    @Override
    public List<UserResponseDTO> getAllUsersForAdmin() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean modifyUserActivity(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        return user.getIsActive();
    }


}
