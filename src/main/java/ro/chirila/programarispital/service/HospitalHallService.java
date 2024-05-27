package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.HospitalHallRequestDTO;
import ro.chirila.programarispital.repository.dto.HospitalHallResponseDTO;

import java.util.List;

@Service
public interface HospitalHallService {

    HospitalHallResponseDTO addHospitalHall(HospitalHallRequestDTO hospitalHallRequestDTO);
    List<HospitalHallResponseDTO> getAllHospitalHalls();

    void deleteHospitalHallById(Long id);

    HospitalHallResponseDTO updateHospitalHall(Long hallId, HospitalHallRequestDTO hospitalHallRequestDTO);
    HospitalHallResponseDTO getHospitalHallById(Long hallId);
}
