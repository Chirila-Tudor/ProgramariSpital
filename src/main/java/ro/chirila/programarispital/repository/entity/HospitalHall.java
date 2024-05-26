package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hall")
@Getter
@Setter
public class HospitalHall {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_hall")
    private Long id;

    private String roomName;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "hospital_equipment",
            joinColumns = {@JoinColumn(name = "id_hall")},
            inverseJoinColumns = {@JoinColumn(name = "id_equipment")}
    )
    @JsonIgnoreProperties("equipmentList")
    private List<Equipment> equipment;

    @JsonIgnoreProperties("hospitalHall")
    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    private User doctor;

}
