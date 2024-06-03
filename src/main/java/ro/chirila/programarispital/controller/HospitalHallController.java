package ro.chirila.programarispital.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.HospitalHallRequestDTO;
import ro.chirila.programarispital.repository.dto.HospitalHallResponseDTO;
import ro.chirila.programarispital.service.HospitalHallService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hall")
public class HospitalHallController {
    private final HospitalHallService hospitalHallService;

    public HospitalHallController(HospitalHallService hospitalHallService) {
        this.hospitalHallService = hospitalHallService;
    }

    @Transactional
    @PostMapping("/add-hall")
    public ResponseEntity<HospitalHallResponseDTO> addHospitalHall(@RequestBody HospitalHallRequestDTO hallRequest) {
        HospitalHallResponseDTO responseDTO = hospitalHallService.addHospitalHall(hallRequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/get-all-halls")
    public ResponseEntity<List<HospitalHallResponseDTO>> getAllAppointments() {
        return new ResponseEntity<>(hospitalHallService.getAllHospitalHalls(), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-hall")
    public ResponseEntity<String> deleteHall(@RequestParam Long id) {
        hospitalHallService.deleteHospitalHallById(id);
        return new ResponseEntity<>("Hall successfully deleted!", HttpStatus.OK);
    }

    @PutMapping("/update-hall")
    public ResponseEntity<HospitalHallResponseDTO> updateHospitalHall(
            @RequestParam("hallId") Long hallId,
            @RequestBody HospitalHallRequestDTO hospitalHallRequestDTO) {
        HospitalHallResponseDTO updatedHall = hospitalHallService.updateHospitalHall(hallId, hospitalHallRequestDTO);
        return ResponseEntity.ok(updatedHall);
    }

    @GetMapping("/getHall")
    public ResponseEntity<HospitalHallResponseDTO> getHospitalHall(@RequestParam Long hallId) {
        try {
            HospitalHallResponseDTO hall = hospitalHallService.getHospitalHallById(hallId);
            return new ResponseEntity<>(hall, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getAppointmentsByHall")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByHospitalHall(@RequestParam Long hallId) {
        List<AppointmentResponseDTO> appointments = hospitalHallService.getAppointmentsByHospitalHallId(hallId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/getDoctorByHall")
    public ResponseEntity<HospitalHallResponseDTO> getHospitalHallByDoctor(@RequestParam String doctorUsername) {
        HospitalHallResponseDTO hospitalHall = hospitalHallService.getHospitalHallByDoctor(doctorUsername);
        return ResponseEntity.ok(hospitalHall);
    }
}
