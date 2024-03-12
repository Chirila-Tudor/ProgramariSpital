package ro.chirila.programarispital.controller;


import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.ChangePasswordDTO;
import ro.chirila.programarispital.repository.dto.UserResponseDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;
import ro.chirila.programarispital.service.UserService;

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
    public ResponseEntity<UserResponseDTO> addUser(@RequestParam(name = "username") String username){
        return new ResponseEntity<>(userService.addUser(username), HttpStatus.CREATED);
    }
    @Transactional
    @GetMapping
    public UserResponseDTO getUserByUsername(@RequestParam(name = "username") String username){
        return userService.getUserByUsername(username);
    }
    @Transactional
    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        boolean passwordChanged = userService.changePassword(changePasswordDTO);
        if(passwordChanged){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ResponseEntity<Object> entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return entity;
    }
    @PostMapping("/login")
    public UserSecurityDTO login(@RequestParam(name = "username") String username, @RequestBody String hashPassword){
        return userService.login(username,hashPassword);
    }
    @Transactional
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@RequestBody String username){
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.OK);
    }

}
