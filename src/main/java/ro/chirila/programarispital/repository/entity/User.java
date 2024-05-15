package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Data
@Setter
@Getter
public class User {

    //region Constructors
    public User(Long id, String username, String password, Boolean hasPassword, Role role, List<Appointment> appointments,
                List<TypeOfService> services, Boolean isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hasPassword = hasPassword;
        this.role = role;
        this.appointments = appointments;
        this.services = services;
        this.isActive = isActive;
    }

    public User() {
    }
    //endregion

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_person")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private Boolean hasPassword;

    private Role role;

    private Boolean isActive;

    private Boolean isFirstLogin;

    @JsonIgnoreProperties("scheduledPerson")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scheduledPerson")
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "doctor_service",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<TypeOfService> services = new ArrayList<>();

}
