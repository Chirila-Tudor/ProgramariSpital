package ro.chirila.programarispital.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.AppointmentNotFoundException;
import ro.chirila.programarispital.repository.AppointmentRepository;
import ro.chirila.programarispital.repository.HospitalHallRepository;
import ro.chirila.programarispital.repository.TypeOfServiceRepository;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceDTO;
import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.HospitalHall;
import ro.chirila.programarispital.repository.entity.TypeOfService;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.AppointmentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TypeOfServiceRepository typeOfServiceRepository;

    private final HospitalHallRepository hospitalHallRepository;


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ModelMapper modelMapper, UserRepository userRepository, TypeOfServiceRepository typeOfServiceRepository, HospitalHallRepository hospitalHallRepository) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.typeOfServiceRepository = typeOfServiceRepository;
        this.hospitalHallRepository = hospitalHallRepository;

    }

    @Override
    public AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointment, String username) {
        Appointment savedAppointment = new Appointment();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        User newUser = null;
        if (optionalUser.isEmpty()) {
            newUser = new User();
            newUser.setUsername(username);
            newUser.setHasPassword(false);
            newUser.setIsActive(false);
            userRepository.save(newUser);
            savedAppointment.setScheduledPerson(newUser);
        } else {
            savedAppointment.setScheduledPerson(optionalUser.get());
        }
        savedAppointment.setEmail(appointment.getEmail());
        savedAppointment.setFirstName(appointment.getFirstName());
        savedAppointment.setLastName(appointment.getLastName());
        savedAppointment.setPhoneNumber(appointment.getPhoneNumber());
        savedAppointment.setDateOfBirth(appointment.getDateOfBirth());
        savedAppointment.setChooseDate(appointment.getChooseDate());
        savedAppointment.setAppointmentHour(appointment.getAppointmentHour());
        savedAppointment.setPeriodOfAppointment(appointment.getPeriodOfAppointment());
        savedAppointment.setTypeOfServices(new ArrayList<>());

        for (TypeOfServiceDTO typeOfServiceDTO : appointment.getTypeOfServices()) {
            TypeOfService typeOfService = typeOfServiceRepository.findByService(modelMapper.map(typeOfServiceDTO, TypeOfService.class).getService());

            if (typeOfService == null) {
                savedAppointment.getTypeOfServices().add(modelMapper.map(typeOfServiceDTO, TypeOfService.class));
            } else {
                savedAppointment.getTypeOfServices().add(typeOfService);
            }
        }
        HospitalHall hospitalHall = hospitalHallRepository.findByRoomName(appointment.getHospitalHallName())
                .orElseThrow(() -> new IllegalArgumentException("HospitalHall not found"));

        boolean isConflict = appointmentRepository.existsByHospitalHallAndChooseDateAndAppointmentHour(
                hospitalHall, savedAppointment.getChooseDate(), savedAppointment.getAppointmentHour());

        if (isConflict) {
            throw new IllegalArgumentException("An appointment already exists at this time and room.");
        }
        savedAppointment.setHospitalHall(hospitalHall);

        if (optionalUser.isEmpty()) {
            newUser.getAppointments().add(savedAppointment);
        } else {
            optionalUser.get().getAppointments().add(savedAppointment);
        }
        appointmentRepository.save(savedAppointment);
        return new AppointmentResponseDTO(savedAppointment.getId(), savedAppointment.getEmail(), savedAppointment.getFirstName(),
                savedAppointment.getLastName(), savedAppointment.getPhoneNumber(), savedAppointment.getDateOfBirth(), savedAppointment.getChooseDate(), savedAppointment.getAppointmentHour(),
                savedAppointment.getPeriodOfAppointment(), savedAppointment.getTypeOfServices().stream().map(typeOfService -> new TypeOfServiceDTO(typeOfService.getService())).toList(),
                savedAppointment.getScheduledPerson().getUsername());

    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        Appointment appointment = optionalAppointment.get();

        appointment.setChooseDate(appointmentUpdateDTO.getChooseDate());
        appointment.setAppointmentHour(appointmentUpdateDTO.getAppointmentHour());
        appointment.setPeriodOfAppointment(appointmentUpdateDTO.getPeriodOfAppointment());

        appointmentRepository.save(appointment);

        return modelMapper.map(appointment, AppointmentResponseDTO.class);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            throw new AppointmentNotFoundException("Appointment doesn't exists.");
        }
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream().map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class)).toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAllFutureAppointments() {
        LocalDate currentDate = LocalDate.now();
        List<Appointment> appointments = appointmentRepository.findAllFutureAppointments(currentDate);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class)).toList();
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Appointment doesn't exists."));
        return modelMapper.map(appointment, AppointmentResponseDTO.class);
    }
}
