package ro.chirila.programarispital.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.TypeOfServiceNotFoundException;
import ro.chirila.programarispital.repository.TypeOfServiceRepository;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.TypeOfServiceRequestDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceResponseDTO;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.TypeOfService;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.TypeOfWorkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TypeOfWorkServiceImpl implements TypeOfWorkService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final TypeOfServiceRepository typeOfServiceRepository;

    public TypeOfWorkServiceImpl(UserRepository userRepository, ModelMapper modelMapper, TypeOfServiceRepository typeOfServiceRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.typeOfServiceRepository = typeOfServiceRepository;
    }

    @Override
    public TypeOfServiceResponseDTO addService(TypeOfServiceRequestDTO typeOfServiceRequestDTO) {
        TypeOfService typeOfService = new TypeOfService();

        typeOfService.setService(typeOfServiceRequestDTO.getService());
        List<String> doctorUsernames = typeOfServiceRequestDTO.getDoctorsWhoCanPerformService();
        List<User> doctors = new ArrayList<>();

        for (String doctorUsername : doctorUsernames) {
            Optional<User> optionalDoctor = userRepository.findByUsername(doctorUsername);
            if (optionalDoctor.isEmpty()) {
                throw new RuntimeException("Doctor not found: " + doctorUsername);
            }
            User doctor = optionalDoctor.get();
            if (doctor.getRole() != Role.DOCTOR) {
                throw new RuntimeException("The specified user is not a doctor: " + doctorUsername);
            }
            doctors.add(doctor);
        }

        typeOfService.setDoctorsWhoCanPerformService(doctors);

        typeOfServiceRepository.save(typeOfService);

        for (User doctor : doctors) {
            List<TypeOfService> services = doctor.getServices();
            if (services == null) {
                services = new ArrayList<>();
            }
            services.add(typeOfService);
            doctor.setServices(services);
            userRepository.save(doctor);
        }
        List<String> usernames = new ArrayList<>();
        for(User user : doctors){
            usernames.add(user.getUsername());
        }
        return new TypeOfServiceResponseDTO(typeOfService.getId(), typeOfService.getService(), usernames);
    }

    @Override
    public List<TypeOfServiceResponseDTO> getAllTypeOfServices() {
        List<TypeOfService> typeOfServices = typeOfServiceRepository.findAll();
        List<TypeOfServiceResponseDTO> typeOfServiceResponseDTOS = new ArrayList<>();
        for(TypeOfService typeOfService : typeOfServices){
            List<String> usernames = new ArrayList<>();
            for(User user : typeOfService.getDoctorsWhoCanPerformService()){
                usernames.add(user.getUsername());
            }
            typeOfServiceResponseDTOS.add(new TypeOfServiceResponseDTO(typeOfService.getId(),typeOfService.getService(),usernames));
        }
        return typeOfServiceResponseDTOS;
    }

    @Override
    public TypeOfServiceResponseDTO getTypeOfServiceById(Long serviceId) {
        TypeOfService typeOfService = typeOfServiceRepository.findById(serviceId).orElseThrow(() -> new TypeOfServiceNotFoundException("Hall doesn't exists."));
        return modelMapper.map(typeOfService, TypeOfServiceResponseDTO.class);
    }


}
