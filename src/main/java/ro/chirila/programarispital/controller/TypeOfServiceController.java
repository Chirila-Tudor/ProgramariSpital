package ro.chirila.programarispital.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.programarispital.repository.dto.TypeOfServiceRequestDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceResponseDTO;
import ro.chirila.programarispital.service.TypeOfWorkService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/service")
public class TypeOfServiceController {
    private final TypeOfWorkService typeOfWorkService;

    public TypeOfServiceController(TypeOfWorkService typeOfWorkService) {
        this.typeOfWorkService = typeOfWorkService;
    }

    @PostMapping("/addService")
    public ResponseEntity<TypeOfServiceResponseDTO> addService(@RequestBody TypeOfServiceRequestDTO typeOfServiceRequestDTO) {
        try {
            TypeOfServiceResponseDTO response = typeOfWorkService.addService(typeOfServiceRequestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @GetMapping("/getAllServices")
    public ResponseEntity<List<TypeOfServiceResponseDTO>> getAllServices() {
        return new ResponseEntity<>(typeOfWorkService.getAllTypeOfServices(), HttpStatus.OK);
    }

    @GetMapping("/getService")
    public ResponseEntity<TypeOfServiceResponseDTO> getHospitalHall(@RequestParam Long serviceId) {
        try {
            TypeOfServiceResponseDTO typeOfServiceResponseDTO = typeOfWorkService.getTypeOfServiceById(serviceId);
            return new ResponseEntity<>(typeOfServiceResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getDoctorsByService")
    public ResponseEntity<List<String>> getDoctorsByTypeOfService(@RequestParam Long serviceId) {
        List<String> doctorUsernames = typeOfWorkService.getDoctorsByTypeOfService(serviceId);
        return ResponseEntity.ok(doctorUsernames);
    }
}
