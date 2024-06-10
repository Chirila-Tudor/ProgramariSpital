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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        TypeOfService selectedService = typeOfServiceRepository.findByService(appointment.getTypeOfServices().get(0).getService());
        if (selectedService == null) {
            throw new IllegalArgumentException("Selected service not found: " + appointment.getTypeOfServices().get(0).getService());
        }
        savedAppointment.getTypeOfServices().add(selectedService);

        User selectedDoctor = null;
        for (User doctor : selectedService.getDoctorsWhoCanPerformService()) {
            boolean isDoctorAvailable = !appointmentRepository.existsByDoctorAndChooseDateAndAppointmentHour(
                    doctor, savedAppointment.getChooseDate(), savedAppointment.getAppointmentHour());
            if (isDoctorAvailable) {
                selectedDoctor = doctor;
                break;
            }
        }

        if (selectedDoctor == null) {
            throw new IllegalArgumentException("No available doctors for the selected service at the chosen date and time.");
        }
        savedAppointment.setDoctor(selectedDoctor);

        HospitalHall hospitalHall = hospitalHallRepository.findByRoomName(appointment.getHospitalHallName())
                .orElseThrow(() -> new IllegalArgumentException("HospitalHall not found"));

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
                savedAppointment.getScheduledPerson().getUsername(),savedAppointment.getDoctor().getUsername());

    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentUpdateDTO appointmentUpdateDTO) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        Appointment appointment = optionalAppointment.get();

        User doctor = appointment.getDoctor();

        boolean isDoctorAvailable = !appointmentRepository.existsByDoctorAndChooseDateAndAppointmentHour(
                doctor, appointmentUpdateDTO.getChooseDate(), appointmentUpdateDTO.getAppointmentHour());

        if (!isDoctorAvailable) {
            throw new IllegalArgumentException("The doctor is not available for the selected date and time.");
        }
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

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByScheduledPerson(String username) {
        List<Appointment> appointments = appointmentRepository.findByScheduledPersonUsername(username);
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class)).toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsForDoctor(String doctorUsername) {
        List<Appointment> appointments = appointmentRepository.findByDoctorUsername(doctorUsername);

        if (appointments.isEmpty()) {
            throw new IllegalArgumentException("No appointments found for the doctor with username: " + doctorUsername);
        }
        return appointments.stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class))
                .toList();
    }

    @Override
    public List<String> getAvailableTimes(String chooseDate, String service, String doctorUsername) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<String> availableTimes = new ArrayList<>();
        
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        TypeOfService selectedService = typeOfServiceRepository.findByService(service);
        if (selectedService == null) {
            throw new IllegalArgumentException("Selected service not found: " + service);
        }

        Optional<User> optionalDoctor = userRepository.findByUsername(doctorUsername);
        if (optionalDoctor.isEmpty()) {
            throw new IllegalArgumentException("Doctor not found: " + doctorUsername);
        }
        User doctor = optionalDoctor.get();

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
            boolean isTimeAvailable = !appointmentRepository.existsByDoctorAndChooseDateAndAppointmentHour(
                    doctor, chooseDate, time.format(timeFormatter));

            if (isTimeAvailable) {
                availableTimes.add(time.format(timeFormatter));
            }
        }

        return availableTimes;
    }


}
