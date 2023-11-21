package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table
public class TypeOfService {

    //region Constructors
    public TypeOfService(Long id, String service, List<Appointment> appointmentList, List<User> doctors) {
        this.id = id;
        this.service = service;
        this.appointmentList = appointmentList;
        this.doctors = doctors;
    }
    public TypeOfService() {

    }
    //endregion

    //region Getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public List<User> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<User> doctors) {
        this.doctors = doctors;
    }
    //endregion

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_service")
    private Long id;
    private String service;

    @ManyToMany(mappedBy = "typeOfServices")
    @JsonIgnoreProperties("typeOfServices")
    private List<Appointment> appointmentList;

    @ManyToMany(mappedBy = "services")
    @JsonIgnoreProperties("services")
    private List<User> doctors;

}
