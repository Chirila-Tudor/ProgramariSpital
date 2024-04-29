
package ro.chirila.programarispital.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;
import ro.chirila.programarispital.service.AppointmentService;
import ro.chirila.programarispital.service.SendEmailService;

import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SendEmailService sendEmailService;

    public AppointmentController(AppointmentService appointmentService, SendEmailService sendEmailService) {
        this.appointmentService = appointmentService;
        this.sendEmailService = sendEmailService;
    }
    @PostMapping("/create-appointment")
    @Transactional
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO
            , @RequestParam String username){

        AppointmentResponseDTO appointmentResponseDTO = appointmentService.addAppointment(appointmentRequestDTO,username);
        CompletableFuture.runAsync(() -> sendEmailService.sendAppointmentEmail(appointmentResponseDTO));
        return new ResponseEntity<>(appointmentResponseDTO, HttpStatus.OK);
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
