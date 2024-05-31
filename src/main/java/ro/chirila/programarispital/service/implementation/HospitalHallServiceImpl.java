package ro.chirila.programarispital.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.AppointmentNotFoundException;
import ro.chirila.programarispital.exception.HospitalHallNotFoundException;
import ro.chirila.programarispital.repository.EquipmentRepository;
import ro.chirila.programarispital.repository.HospitalHallRepository;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.EquipmentDTO;
import ro.chirila.programarispital.repository.dto.HospitalHallRequestDTO;
import ro.chirila.programarispital.repository.dto.HospitalHallResponseDTO;
import ro.chirila.programarispital.repository.entity.Equipment;
import ro.chirila.programarispital.repository.entity.HospitalHall;
import ro.chirila.programarispital.repository.entity.Role;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.HospitalHallService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalHallServiceImpl implements HospitalHallService {

    private final HospitalHallRepository hospitalHallRepository;


    private final EquipmentRepository equipmentRepository;


    private final UserRepository userRepository;


    private final ModelMapper modelMapper;

    public HospitalHallServiceImpl(HospitalHallRepository hospitalHallRepository, EquipmentRepository equipmentRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.hospitalHallRepository = hospitalHallRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HospitalHallResponseDTO addHospitalHall(HospitalHallRequestDTO hospitalHallRequestDTO) {
        HospitalHall hospitalHall = new HospitalHall();

        Optional<User> optionalDoctor = userRepository.findByUsername(hospitalHallRequestDTO.getDoctorUsername());
        if (optionalDoctor.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }
        User doctor = optionalDoctor.get();
        if (doctor.getRole() != Role.DOCTOR) { // Assuming Role is an enum and user has a getRole() method
            throw new RuntimeException("The specified user is not a doctor");
        }

        hospitalHall.setDoctor(doctor);
        hospitalHall.setRoomName(hospitalHallRequestDTO.getRoom());

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentDTO equipmentDTO : hospitalHallRequestDTO.getEquipment()) {
            Equipment equipment = equipmentRepository.findByName(equipmentDTO.getName());
            if (equipment == null) {
                equipment = new Equipment();
                equipment.setName(equipmentDTO.getName());
                equipmentRepository.save(equipment);
            }
            equipmentList.add(equipment);
        }
        hospitalHall.setEquipment(equipmentList);

        hospitalHallRepository.save(hospitalHall);
        return new HospitalHallResponseDTO(hospitalHall.getId(), hospitalHall.getRoomName(),
                hospitalHall.getEquipment().stream()
                        .map(equipment -> new EquipmentDTO(equipment.getName())).collect(Collectors.toList()),
                hospitalHall.getDoctor().getUsername());
    }

    @Override
    public List<HospitalHallResponseDTO> getAllHospitalHalls() {
        List<HospitalHall> hospitalHalls = hospitalHallRepository.findAll();
        return hospitalHalls.stream().map(hospitalHall -> modelMapper.map(hospitalHall, HospitalHallResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteHospitalHallById(Long id) {
        if (hospitalHallRepository.existsById(id)) {
            hospitalHallRepository.deleteById(id);
        } else {
            throw new AppointmentNotFoundException("Hall doesn't exists.");
        }
    }

    @Override
    public HospitalHallResponseDTO updateHospitalHall(Long hallId, HospitalHallRequestDTO hospitalHallRequestDTO) {
        Optional<HospitalHall> optionalHospitalHall = hospitalHallRepository.findById(hallId);
        if (optionalHospitalHall.isEmpty()) {
            throw new RuntimeException("Hospital Hall not found");
        }
        HospitalHall hospitalHall = optionalHospitalHall.get();

        if (hospitalHallRequestDTO.getDoctorUsername() != null) {
            User doctor = userRepository.findByUsername(hospitalHallRequestDTO.getDoctorUsername())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            if (doctor.getRole() != Role.DOCTOR) {
                throw new RuntimeException("The specified user is not a doctor");
            }
            hospitalHall.setDoctor(doctor);
        }

        if (hospitalHallRequestDTO.getRoom() != null) {
            hospitalHall.setRoomName(hospitalHallRequestDTO.getRoom());
        }

        if (hospitalHallRequestDTO.getEquipment() != null && !hospitalHallRequestDTO.getEquipment().isEmpty()) {
            List<Equipment> equipmentList = new ArrayList<>();
            for (EquipmentDTO equipmentDTO : hospitalHallRequestDTO.getEquipment()) {
                Equipment equipment = equipmentRepository.findByName(equipmentDTO.getName());
                if (equipment == null) {
                    equipment = modelMapper.map(equipmentDTO, Equipment.class);
                    equipmentRepository.save(equipment);
                }
                equipmentList.add(equipment);
            }
            hospitalHall.setEquipment(equipmentList);
        }

        hospitalHallRepository.save(hospitalHall);
        return modelMapper.map(hospitalHall, HospitalHallResponseDTO.class);
    }

    @Override
    public HospitalHallResponseDTO getHospitalHallById(Long hallId) {
        HospitalHall hospitalHall = hospitalHallRepository.findById(hallId).orElseThrow(()->new HospitalHallNotFoundException("Hall doesn't exists."));
        return modelMapper.map(hospitalHall, HospitalHallResponseDTO.class);
    }
}
