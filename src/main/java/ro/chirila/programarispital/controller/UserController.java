
package ro.chirila.programarispital.controller;


import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.exception.UserAlreadyExistException;
import ro.chirila.programarispital.repository.dto.*;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.service.SendEmailService;
import ro.chirila.programarispital.service.UserService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SendEmailService sendEmailService;

    public UserController(UserService userService, SendEmailService sendEmailService) {
        this.userService = userService;
        this.sendEmailService = sendEmailService;
    }

    @Transactional
    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO userRequest) {
        String username = userRequest.getUsername();
        Role role = userRequest.getRole();
        String email = userRequest.getEmail();

        UserResponseDTO userResponseDTO;
        try {
            userResponseDTO = userService.addUser(username, role, email);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UserExistsDTO userExistsDTO = userService.setPasswordForUser(username, role);
        if (userExistsDTO != null) {
            CompletableFuture.runAsync(() -> sendEmailService.sendPasswordForHospitalPersonal(userExistsDTO, userRequest));
        }

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping
    public UserResponseDTO getUserByUsername(@RequestParam(name = "username") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/login")
    public UserSecurityDTO login(@RequestParam(name = "username") String username, @RequestBody String hashPassword) {
        return userService.login(username, hashPassword);
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@RequestBody String username) {
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean passwordChanged = userService.changePassword(changePasswordDTO);
        if (passwordChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @GetMapping("/isFirstLogin")
    public ResponseEntity<Boolean> isFirstLogin(@RequestParam(name = "username") String username) {
        return new ResponseEntity<>(userService.isFirstLogin(username), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsersForAdmin(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/modifyUserActivity")
    public ResponseEntity<Boolean> modifyUserActivity(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(userService.modifyUserActivity(id), HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/forgotPassword")
    public ResponseEntity<Boolean> forgotPassword(@RequestParam("username") String username) {
        String securityCode = userService.forgotPassword(username);
        String email = userService.getEmailByUsername(username);
        CompletableFuture.runAsync(() -> sendEmailService.sendForgotPasswordEmail(email,securityCode,username));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/requestPassword")
    public ResponseEntity<Boolean> requestPassword(@RequestParam("username") String username,
                                                   @RequestBody String securityCode) {
        String newPassword = userService.requestNewPassword(username, securityCode);
        String email = userService.getEmailByUsername(username);
        CompletableFuture.runAsync(() -> sendEmailService.sendPassword(email,username,newPassword));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/allDoctors")
    public ResponseEntity<List<UserSecurityDTO>> getAllDoctors() {
        List<UserSecurityDTO> doctors = userService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
}
