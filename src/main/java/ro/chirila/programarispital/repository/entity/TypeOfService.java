package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "typeofservices")
public class TypeOfService {

    //region Constructors
    public TypeOfService(Long id, String service, List<Appointment> appointmentList, List<User> doctors) {
        this.id = id;
        this.service = service;
        this.appointmentList = appointmentList;
        this.doctorsWhoCanPerformService = doctors;
    }

    public TypeOfService() {

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
    private List<User> doctorsWhoCanPerformService;

}
