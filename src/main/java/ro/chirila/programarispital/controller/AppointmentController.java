
package ro.chirila.programarispital.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;
import ro.chirila.programarispital.repository.dto.UserExistsDTO;
import ro.chirila.programarispital.service.AppointmentService;
import ro.chirila.programarispital.service.SendEmailService;
import ro.chirila.programarispital.service.UserService;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SendEmailService sendEmailService;
    private final UserService userService;
    public AppointmentController(AppointmentService appointmentService, SendEmailService sendEmailService, UserService userService) {
        this.appointmentService = appointmentService;
        this.sendEmailService = sendEmailService;
        this.userService = userService;
    }

    @PostMapping("/create-appointment")
    @Transactional
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO
            , @RequestParam String username){
        UserExistsDTO userExistsDTO = userService.getUserExistByUsername(username);
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.addAppointment(appointmentRequestDTO,username);
        if(userExistsDTO != null){
            CompletableFuture.runAsync(() -> sendEmailService.sendPasswordEmail(userExistsDTO,appointmentResponseDTO));
        }
        CompletableFuture.runAsync(() -> sendEmailService.sendAppointmentEmail(appointmentResponseDTO));

        return new ResponseEntity<>(appointmentResponseDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    @Transactional
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@RequestParam Long id,
                                                                    @RequestBody AppointmentUpdateDTO appointmentUpdateDTO){
        return new ResponseEntity<>(appointmentService.updateAppointment(id, appointmentUpdateDTO), HttpStatus.OK);
    }
    @DeleteMapping("/delete-appointment")
    @Transactional
    public ResponseEntity<String> deleteAppointment(@RequestParam Long id){
        appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>("Appointment successfully deleted!", HttpStatus.OK);
    }
}
