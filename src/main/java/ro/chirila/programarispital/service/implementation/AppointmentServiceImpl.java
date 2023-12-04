package ro.chirila.programarispital.service.implementation;

import org.jvnet.hk2.annotations.Service;
import org.modelmapper.ModelMapper;
import ro.chirila.programarispital.exception.FieldValidationException;
import ro.chirila.programarispital.exception.UserNotFoundException;
import ro.chirila.programarispital.repository.AppointmentRepository;
import ro.chirila.programarispital.repository.TypeOfServiceRepository;
import ro.chirila.programarispital.repository.UserRepository;
import ro.chirila.programarispital.repository.dto.AppointmentRequestDTO;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceDTO;
import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.TypeOfService;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.AppointmentService;
import ro.chirila.programarispital.service.UserService;

import java.util.ArrayList;
import java.util.Date;

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
        validateFields(appointment);
        Appointment savedAppointment = new Appointment();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("No user found by this username"));
        savedAppointment.setScheduledPerson(user);
        savedAppointment.setEmail(appointment.getEmail());
        savedAppointment.setFirstName(appointment.getFirstName());
        savedAppointment.setLastName(appointment.getLastName());
        savedAppointment.setPhoneNumber(appointment.getPhoneNumber());
        savedAppointment.setDateOfBirth(new Date());
        savedAppointment.setChooseDate(new Date());
        savedAppointment.setAppointmentHour(appointment.getAppointmentHour());
        savedAppointment.setPeriodOfAppointment(appointment.getPeriodOfAppointment());
        savedAppointment.setTypeOfServices(new ArrayList<>());

        for(TypeOfServiceDTO typeOfServiceDTO : appointment.getTypeOfServices()){
            TypeOfService typeOfService = typeOfServiceRepository.findByService(modelMapper.map(typeOfServiceDTO, TypeOfService.class).getService());

            if(typeOfService == null){
                savedAppointment.getTypeOfServices().add(modelMapper.map(typeOfServiceDTO, TypeOfService.class));
            }else{
                savedAppointment.getTypeOfServices().add(typeOfService);
            }
        }

        if(user.getAppointments() == null){
            user.setAppointments(new ArrayList<>());
        }
        user.getAppointments().add(savedAppointment);

        AppointmentResponseDTO responseDTO = modelMapper.map(appointmentRepository.save(savedAppointment), AppointmentResponseDTO.class);
        responseDTO.setScheduledPerson(user);
        return responseDTO;

    }

    public void validateFields(AppointmentRequestDTO appointment){
        if(appointment.getEmail() == null || appointment.getEmail().isEmpty()){
            throw new FieldValidationException("Please enter a valid email.");
        }
        if(appointment.getFirstName() == null || appointment.getFirstName().isEmpty()){
            throw new FieldValidationException("Please enter the first-name.");
        }
        if(appointment.getLastName() == null || appointment.getLastName().isEmpty()){
            throw new FieldValidationException("Please enter the last-name.");
        }
        if(appointment.getPhoneNumber() == null || appointment.getPhoneNumber().isEmpty()){
            throw new FieldValidationException("Please enter a phone number.");
        }
        if(appointment.getDateOfBirth() == null){
            throw new FieldValidationException("Please choose a date of birth");
        }
        if(appointment.getChooseDate() == null){
            throw new FieldValidationException("Please choose the date of the appointment.");
        }
        if(appointment.getAppointmentHour() == null){
            throw new FieldValidationException("Please choose the appointment hour");
        }
        if(appointment.getPeriodOfAppointment() == null) {
            throw new FieldValidationException("Please choose the period of time");
        }
        if(appointment.getTypeOfServices() == null || appointment.getTypeOfServices().size() <= 0) {
            throw new FieldValidationException("Please choose the type of service");
        }
    }
}
