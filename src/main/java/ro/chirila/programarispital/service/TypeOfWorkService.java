package ro.chirila.programarispital.service;

import org.springframework.stereotype.Service;
import ro.chirila.programarispital.repository.dto.TypeOfServiceRequestDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceResponseDTO;

import java.util.List;

@Service
public interface TypeOfWorkService {

    TypeOfServiceResponseDTO addService(TypeOfServiceRequestDTO typeOfServiceRequestDTO);

    List<TypeOfServiceResponseDTO> getAllTypeOfServices();

    TypeOfServiceResponseDTO getTypeOfServiceById(Long serviceId);

    List<String> getDoctorsByTypeOfService(Long serviceId);
}
