package ro.chirila.programarispital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.chirila.programarispital.repository.entity.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    Equipment findByName(String name);

}
