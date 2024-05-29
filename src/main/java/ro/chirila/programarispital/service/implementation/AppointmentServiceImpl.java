package ro.chirila.programarispital.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.programarispital.exception.AppointmentNotFoundException;
import ro.chirila.programarispital.repository.AppointmentRepository;
import ro.chirila.programarispital.repository.TypeOfServiceRepository;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.AppointmentUpdateDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceDTO;
import ro.chirila.programarispital.repository.entity.Appointment;
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


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ModelMapper modelMapper, UserRepository userRepository, TypeOfServiceRepository typeOfServiceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.typeOfServiceRepository = typeOfServiceRepository;
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
        if (optionalUser.isEmpty()) {
            newUser.getAppointments().add(savedAppointment);
        } else {
            optionalUser.get().getAppointments().add(savedAppointment);
        }
        appointmentRepository.save(savedAppointment);
        return new AppointmentResponseDTO(savedAppointment.getEmail(), savedAppointment.getFirstName(),
                savedAppointment.getLastName(), savedAppointment.getPhoneNumber(), savedAppointment.getDateOfBirth(), savedAppointment.getChooseDate(), savedAppointment.getAppointmentHour(),
                savedAppointment.getPeriodOfAppointment(), savedAppointment.getTypeOfServices().stream().map(typeOfService -> new TypeOfServiceDTO(typeOfService.getService())).toList(),
                savedAppointment.getScheduledPerson().getUsername());

    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO) {
        /*if (appointmentRepository.findById(id).isPresent()) {
            Appointment appointment = appointmentRepository.findById(id).get();

            if (appointmentUpdateDTO.getEmail() != null) {
                appointment.setEmail(appointmentUpdateDTO.getEmail());
            }
            if (appointmentUpdateDTO.getEmail() != null) {
                appointment.setFirstName(appointmentUpdateDTO.getFirstName());
            }
            if (appointmentUpdateDTO.getLastName() != null) {
                appointment.setLastName(appointmentUpdateDTO.getLastName());
            }
            if (appointmentUpdateDTO.getPhoneNumber() != null) {
                appointment.setPhoneNumber(appointmentUpdateDTO.getPhoneNumber());
            }
            if (appointmentUpdateDTO.getDateOfBirth() != null) {
                appointment.setDateOfBirth(appointmentUpdateDTO.getDateOfBirth());
            }
            if (appointmentUpdateDTO.getChooseDate() != null) {
                appointment.setChooseDate(appointmentUpdateDTO.getChooseDate());
            }
            if (appointmentUpdateDTO.getAppointmentHour() != null) {
                appointment.setAppointmentHour(appointmentUpdateDTO.getAppointmentHour());
            }
            if (appointmentUpdateDTO.getAppointmentHour() != null) {
                appointment.setPeriodOfAppointment(appointmentUpdateDTO.getPeriodOfAppointment());
            }

            if (appointmentUpdateDTO.getTypeOfService() != null) {
                if (appointmentUpdateDTO.getTypeOfService().isEmpty()) {
                    throw new RuntimeException("Please select at least one type of service.");
                }
                List<TypeOfService> newTypeOfServiceList = new ArrayList<>();

                for (TypeOfServiceDTO typeOfServiceDTO : appointmentUpdateDTO.getTypeOfService()) {

                    TypeOfService newTypeOfService = typeOfServiceRepository.findByService(typeOfServiceDTO.getService());
                    if (newTypeOfService == null) {
                        TypeOfService addedTypeOfService = new TypeOfService();
                        addedTypeOfService.setService(typeOfServiceDTO.getService());
                        typeOfServiceRepository.save(addedTypeOfService);
                        newTypeOfServiceList.add(addedTypeOfService);
                    } else {
                        newTypeOfServiceList.add(newTypeOfService);
                    }
                }
                appointment.setTypeOfServices(newTypeOfServiceList);
            }

            AppointmentResponseDTO responseDTO = modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDTO.class);
            //responseDTO.setScheduledPerson(appointmentRepository.findById(id).get().getScheduledPerson());
            return responseDTO;

        } else {
            throw new AppointmentNotFoundException("Appointment doesn't exist.");
        }*/
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


}
