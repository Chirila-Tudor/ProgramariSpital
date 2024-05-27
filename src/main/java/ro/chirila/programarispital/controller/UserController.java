
package ro.chirila.programarispital.controller;


import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.*;
import ro.chirila.programarispital.service.UserService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@RequestParam(name = "username") String username) {
        return new ResponseEntity<>(userService.addUser(username), HttpStatus.CREATED);
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
    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean passwordChanged = userService.changePassword(changePasswordDTO);
        if (passwordChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @GetMapping("/is-first-login")
    public ResponseEntity<Boolean> isFirstLogin(@RequestParam(name = "username") String username) {
        return new ResponseEntity<>(userService.isFirstLogin(username), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsersForAdmin(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/modify-user-activity")
    public ResponseEntity<Boolean> modifyUserActivity(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(userService.modifyUserActivity(id), HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/forgot-password")
    public ResponseEntity<Boolean> requestNewPassword(@RequestParam("username") String username) {
        return new ResponseEntity<>(userService.forgotPassword(username), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/request-password")
    public ResponseEntity<Boolean> requestPassword(@RequestParam("username") String username,
                                                   @RequestBody String securityCode) {
        return new ResponseEntity<>(userService.requestNewPassword(username, securityCode), HttpStatus.OK);
    }


}
