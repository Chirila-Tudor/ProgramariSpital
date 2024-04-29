package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
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

    //region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<TypeOfService> getServices() {
        return services;
    }

    public void setServices(List<TypeOfService> services) {
        this.services = services;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
