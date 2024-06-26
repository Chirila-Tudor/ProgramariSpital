package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

    //region Constructors
    public Appointment(Long id, String email, String firstName, String lastName, String dateOfBirth, String phoneNumber, List<TypeOfService> typeOfServices,
                       String chooseDate, String appointmentHour, User scheduledPerson, PeriodOfAppointment periodOfAppointment, HospitalHall hospitalHall) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.typeOfServices = typeOfServices;
        this.chooseDate = chooseDate;
        this.appointmentHour = appointmentHour;
        this.scheduledPerson = scheduledPerson;
        this.periodOfAppointment = periodOfAppointment;
        this.hospitalHall = hospitalHall;
    }

    public Appointment() {

    }
    //endregion

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_appointment")
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private String chooseDate;
    private String appointmentHour;
    private PeriodOfAppointment periodOfAppointment;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointment_service",
            joinColumns = {@JoinColumn(name = "id_appointment")},
            inverseJoinColumns = {@JoinColumn(name = "id_service")}
    )
    @JsonIgnoreProperties("appointmentList")
    private List<TypeOfService> typeOfServices = new ArrayList<>();

    @JsonIgnoreProperties("appointments")
    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    private User scheduledPerson;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @JsonIgnoreProperties("appointments")
    @ManyToOne
    @JoinColumn(name = "id_hall", referencedColumnName = "id_hall")
    private HospitalHall hospitalHall;

}
