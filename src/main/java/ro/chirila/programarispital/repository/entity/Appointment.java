package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appointment")
public class Appointment {

    //region Constructors
    public Appointment(Long id, String email, String firstName, String lastName, Date dateOfBirth, String phoneNumber, List<TypeOfService> typeOfServices,
                       Date chooseDate, LocalTime appointmentHour, User scheduledPerson, PeriodOfAppointment periodOfAppointment) {
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
    }
    public Appointment() {

    }
    //endregion


    //region Getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<TypeOfService> getTypeOfServices() {
        return typeOfServices;
    }

    public void setTypeOfServices(List<TypeOfService> typeOfServices) {
        this.typeOfServices = typeOfServices;
    }

    public Date getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(Date chooseDate) {
        this.chooseDate = chooseDate;
    }

    public LocalTime getAppointmentHour() {
        return appointmentHour;
    }

    public void setAppointmentHour(LocalTime appointmentHour) {
        this.appointmentHour = appointmentHour;
    }

    public User getScheduledPerson() {
        return scheduledPerson;
    }

    public void setScheduledPerson(User scheduledPerson) {
        this.scheduledPerson = scheduledPerson;
    }

    public PeriodOfAppointment getPeriodOfAppointment() {
        return periodOfAppointment;
    }

    public void setPeriodOfAppointment(PeriodOfAppointment periodOfAppointment) {
        this.periodOfAppointment = periodOfAppointment;
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
    private Date dateOfBirth;
    private Date chooseDate;
    private LocalTime appointmentHour;
    private PeriodOfAppointment periodOfAppointment;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointment_service",
            joinColumns = {@JoinColumn(name = "id_appointment")},
            inverseJoinColumns = {@JoinColumn(name = "id_service")}
    )
    @JsonIgnoreProperties("appointmentList")
    private List<TypeOfService> typeOfServices;

    @JsonIgnoreProperties("appointments")
    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    private User scheduledPerson;



}
