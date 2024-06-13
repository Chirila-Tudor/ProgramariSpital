
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/createAppointment")
    @Transactional
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO
            , @RequestParam String username) {
        UserExistsDTO userExistsDTO = userService.setPasswordForPatient(username, appointmentRequestDTO.getEmail());
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.addAppointment(appointmentRequestDTO, username);
        if (userExistsDTO != null) {
            CompletableFuture.runAsync(() -> sendEmailService.sendPasswordEmail(userExistsDTO, appointmentResponseDTO));
        }
        CompletableFuture.runAsync(() -> sendEmailService.sendAppointmentEmail(appointmentResponseDTO));

        return new ResponseEntity<>(appointmentResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/updateAppointment")
    @Transactional
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@RequestParam Long id,
                                                                    @RequestBody AppointmentUpdateDTO appointmentUpdateDTO) {
        CompletableFuture.runAsync(() -> sendEmailService.sendUpdatedAppointmentEmail(appointmentUpdateDTO,id));
        return new ResponseEntity<>(appointmentService.updateAppointment(id, appointmentUpdateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/deleteAppointment")
    @Transactional
    public ResponseEntity<String> deleteAppointment(@RequestParam Long id) {
        appointmentService.deleteAppointmentById(id);
        return new ResponseEntity<>("Appointment successfully deleted!", HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/getAllAppointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/getFutureAppointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllFutureAppointments() {
        return new ResponseEntity<>(appointmentService.getAllFutureAppointments(), HttpStatus.OK);
    }
    @Transactional
    @GetMapping("/getAppointment")
    public ResponseEntity<AppointmentResponseDTO> getAppointment(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getAppointmentForUser")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByScheduledPerson(@RequestParam String username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByScheduledPerson(username), HttpStatus.OK);
    }

    @GetMapping("/getAllAppointmentsByDoctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForDoctor(@RequestParam String username) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsForDoctor(username);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getAvailableTimes")
    public ResponseEntity<List<String>> getAvailableTimes(
            @RequestParam String chooseDate,
            @RequestParam Long idService,
            @RequestParam String doctorUsername) {
        List<String> availableTimes = appointmentService.getAvailableTimes(chooseDate, idService, doctorUsername);
        return ResponseEntity.ok(availableTimes);
    }

    @GetMapping("/getDateAvailability")
    public ResponseEntity<Map<String, Object>> getDoctorDateAvailability(
            @RequestParam String chooseDate,
            @RequestParam Long idService,
            @RequestParam String doctorUsername) {

        boolean isAvailable = appointmentService.isDoctorAvailableOnDate(chooseDate, idService, doctorUsername);

        Map<String, Object> response = new HashMap<>();
        response.put("isAvailable", isAvailable);

        return ResponseEntity.ok(response);
    }
}
