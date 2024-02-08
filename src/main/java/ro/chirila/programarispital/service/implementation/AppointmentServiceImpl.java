package ro.chirila.programarispital.service.implementation;

import org.jvnet.hk2.annotations.Service;
import org.modelmapper.ModelMapper;
import ro.chirila.programarispital.exception.AppointmentNotFoundException;
import ro.chirila.programarispital.exception.UserNotFoundException;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("No user found by this username"));

        savedAppointment.setScheduledPerson(user);
        savedAppointment.setEmail(appointment.email());
        savedAppointment.setFirstName(appointment.firstName());
        savedAppointment.setLastName(appointment.lastName());
        savedAppointment.setPhoneNumber(appointment.phoneNumber());
        savedAppointment.setDateOfBirth(new Date());
        savedAppointment.setChooseDate(new Date());
        savedAppointment.setAppointmentHour(appointment.appointmentHour());
        savedAppointment.setPeriodOfAppointment(appointment.periodOfAppointment());
        savedAppointment.setTypeOfServices(new ArrayList<>());

        for (TypeOfServiceDTO typeOfServiceDTO : appointment.typeOfServices()) {
            TypeOfService typeOfService = typeOfServiceRepository.findByService(modelMapper.map(typeOfServiceDTO, TypeOfService.class).getService());

            if (typeOfService == null) {
                savedAppointment.getTypeOfServices().add(modelMapper.map(typeOfServiceDTO, TypeOfService.class));
            } else {
                savedAppointment.getTypeOfServices().add(typeOfService);
            }
        }

        user.getAppointments().add(savedAppointment);

        AppointmentResponseDTO responseDTO = modelMapper.map(appointmentRepository.save(savedAppointment), AppointmentResponseDTO.class);
        responseDTO.setScheduledPerson(user);
        return responseDTO;
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO) {
        if (appointmentRepository.findById(id).isPresent()) {
            Appointment appointment = appointmentRepository.findById(id).get();

            if (appointmentUpdateDTO.email() != null) {
                appointment.setEmail(appointmentUpdateDTO.email());
            }
            if (appointmentUpdateDTO.firstName() != null) {
                appointment.setFirstName(appointmentUpdateDTO.firstName());
            }
            if (appointmentUpdateDTO.lastName() != null) {
                appointment.setLastName(appointmentUpdateDTO.lastName());
            }
            if (appointmentUpdateDTO.phoneNumber() != null) {
                appointment.setPhoneNumber(appointmentUpdateDTO.phoneNumber());
            }
            if (appointmentUpdateDTO.dateOfBirth() != null) {
                appointment.setDateOfBirth(appointmentUpdateDTO.dateOfBirth());
            }
            if (appointmentUpdateDTO.chooseDate() != null) {
                appointment.setChooseDate(appointmentUpdateDTO.chooseDate());
            }
            if (appointmentUpdateDTO.appointmentHour() != null) {
                appointment.setAppointmentHour(appointmentUpdateDTO.appointmentHour());
            }
            if (appointmentUpdateDTO.periodOfAppointment() != null) {
                appointment.setPeriodOfAppointment(appointmentUpdateDTO.periodOfAppointment());
            }

            if (appointmentUpdateDTO.typeOfService() != null) {
                if (appointmentUpdateDTO.typeOfService().isEmpty()) {
                    throw new RuntimeException("Please select at least one type of service.");
                }
                List<TypeOfService> newTypeOfServiceList = new ArrayList<>();

                for (TypeOfServiceDTO typeOfServiceDTO : appointmentUpdateDTO.typeOfService()) {

                    TypeOfService newTypeOfService = typeOfServiceRepository.findByService(typeOfServiceDTO.service());
                    if (newTypeOfService == null) {
                        TypeOfService addedTypeOfService = new TypeOfService();
                        addedTypeOfService.setService(typeOfServiceDTO.service());
                        typeOfServiceRepository.save(addedTypeOfService);
                        newTypeOfServiceList.add(addedTypeOfService);
                    } else {
                        newTypeOfServiceList.add(newTypeOfService);
                    }
                }
                appointment.setTypeOfServices(newTypeOfServiceList);
            }

            AppointmentResponseDTO responseDTO = modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDTO.class);
            responseDTO.setScheduledPerson(appointmentRepository.findById(id).get().getScheduledPerson());
            return responseDTO;

        } else {
            throw new AppointmentNotFoundException("Appointment doesn't exist.");
        }
    }

    @Override
    public void deleteAppointmentById(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            throw new AppointmentNotFoundException("Appointment doesn't exists.");
        }
    }


}
