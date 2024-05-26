package ro.chirila.programarispital.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.HospitalHallRequestDTO;
import ro.chirila.programarispital.repository.dto.HospitalHallResponseDTO;
import ro.chirila.programarispital.repository.entity.HospitalHall;
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

    @PutMapping("/{hallId}")
    public ResponseEntity<HospitalHallResponseDTO> updateHospitalHall(
            @PathVariable("hallId") Long hallId,
            @RequestBody HospitalHallRequestDTO hospitalHallRequestDTO) {
        HospitalHallResponseDTO updatedHall = hospitalHallService.updateHospitalHall(hallId, hospitalHallRequestDTO);
        return ResponseEntity.ok(updatedHall);
    }

    @GetMapping("/getHall")
    public ResponseEntity<?> getHospitalHall(@RequestParam Long hallId) {
        try {
            HospitalHall hall = hospitalHallService.getHospitalHallById(hallId);
            if (hall == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hall not found");
            }
            return ResponseEntity.ok(hall);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
