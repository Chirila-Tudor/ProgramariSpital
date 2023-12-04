package ro.chirila.programarispital.repository.dto;

import ro.chirila.programarispital.repository.entity.PeriodOfAppointment;
import ro.chirila.programarispital.repository.entity.TypeOfService;
import ro.chirila.programarispital.repository.entity.User;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class AppointmentRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date dateOfBirth;
    private Date chooseDate;
    private LocalTime appointmentHour;
    private PeriodOfAppointment periodOfAppointment;
    private List<TypeOfServiceDTO> typeOfServices;
    private User scheduledPerson;


    //region Getter and setter
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public PeriodOfAppointment getPeriodOfAppointment() {
        return periodOfAppointment;
    }

    public void setPeriodOfAppointment(PeriodOfAppointment periodOfAppointment) {
        this.periodOfAppointment = periodOfAppointment;
    }

    public List<TypeOfServiceDTO> getTypeOfServices() {
        return typeOfServices;
    }

    public void setTypeOfServices(List<TypeOfServiceDTO> typeOfServices) {
        this.typeOfServices = typeOfServices;
    }

    public User getScheduledPerson() {
        return scheduledPerson;
    }

    public void setScheduledPerson(User scheduledPerson) {
        this.scheduledPerson = scheduledPerson;
    }
    //endregion
}
