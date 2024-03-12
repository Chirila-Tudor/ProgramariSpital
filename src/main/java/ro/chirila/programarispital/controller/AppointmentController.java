package ro.chirila.programarispital.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;
import ro.chirila.programarispital.service.AppointmentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping("/create-appointment")
    @Transactional
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO
                                                                    , @RequestParam String username){
        return new ResponseEntity<>(appointmentService.addAppointment(appointmentRequestDTO,username), HttpStatus.OK);
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
