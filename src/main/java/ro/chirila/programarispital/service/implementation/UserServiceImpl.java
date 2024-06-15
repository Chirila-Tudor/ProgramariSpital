package ro.chirila.programarispital.service.implementation;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.BadCredentialsException;
import ro.chirila.programarispital.exception.UserAlreadyDeactivatedException;
import ro.chirila.programarispital.exception.UserAlreadyExistException;
import ro.chirila.programarispital.exception.UserNotFoundException;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.ChangePasswordDTO;
import ro.chirila.programarispital.repository.dto.UserExistsDTO;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
            if (user.getIsActive() && password.equals(user.getPassword())) {
                return modelMapper.map(user, UserSecurityDTO.class);
            }
            if (!user.getIsActive() && password.equals(user.getPassword())) {
                throw new UserAlreadyDeactivatedException("User was deactivated");
            }

        }
        throw new BadCredentialsException("Bad credentials.");
    }

    @Override
    public UserExistsDTO setPasswordForPatient(String username, String email) {
        Optional<User> user = userRepository.findByUsername(username);
        UserExistsDTO newUser = null;
        String password = "";
        if (user.isEmpty()) {
            password = generatePassword(12);
            newUser = new UserExistsDTO(username, hashPassword(password), true, true, Role.Pacient, true,email);
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

        if(!changePasswordDTO.getOldPassword().equals(user.getPassword())){
            return false;
        }
        user.setPassword(changePasswordDTO.getNewPassword());
        user.setIsFirstLogin(false);
        userRepository.save(user);
        return true;

    }

    @Override
    public String forgotPassword(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            int length = 10;
            String securityCode = generateSecurityCode(length);
            user.setSecurityCode(securityCode);
            userRepository.save(user);
            return securityCode;
        }
        throw new BadCredentialsException("Wrong security code");

    }

    @Override
    public String requestNewPassword(String username, String securityCode) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (!user.getSecurityCode().equals(securityCode)) {
            throw new BadCredentialsException("Invalid security code");
        }
        String password = generatePassword(12);
        user.setIsFirstLogin(true);
        user.setPassword(hashPassword(password));
        userRepository.save(user);
        return password;
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
        return users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).toList();
    }

    @Override
    public Boolean modifyUserActivity(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        return user.getIsActive();
    }

    @Override
    public String getEmailByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user.getEmail();
    }

    @Override
    public List<UserSecurityDTO> getAllDoctors() {
        List<User> users = userRepository.findByRole(Role.Doctor);
        return users.stream().map(user -> modelMapper.map(user, UserSecurityDTO.class)).toList();

    }

    @Override
    public List<String> getUserOptions() {
        return Arrays.asList("Admin", "Doctor", "Asistentă");
    }


}
