package ro.chirila.programarispital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.chirila.programarispital.repository.entity.HospitalHall;

import java.util.Optional;

@Repository
public interface HospitalHallRepository extends JpaRepository<HospitalHall, Long> {
    Optional<HospitalHall> findByRoomName(String room);
}
