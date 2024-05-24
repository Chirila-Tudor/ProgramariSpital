package ro.chirila.programarispital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.chirila.programarispital.repository.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE TO_DATE(a.chooseDate, 'YYYY-MM-DD') > :currentDate")
    List<Appointment> findAllFutureAppointments(@Param("currentDate") LocalDate currentDate);
}
