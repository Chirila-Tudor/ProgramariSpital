package ro.chirila.programarispital.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "equipment")
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_equipment")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "equipment")
    @JsonIgnoreProperties("equipment")
    private List<HospitalHall> hospitalHalls;
}
