package ro.chirila.programarispital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.HospitalHall;
import ro.chirila.programarispital.repository.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE TO_DATE(a.chooseDate, 'YYYY-MM-DD') > :currentDate")
    List<Appointment> findAllFutureAppointments(@Param("currentDate") LocalDate currentDate);

    boolean existsByHospitalHallAndChooseDateAndAppointmentHour(HospitalHall hospitalHall, String chooseDate, String appointmentHour);
    List<Appointment> findByScheduledPersonUsername(String username);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
            "WHERE a.doctor = :doctor AND a.chooseDate = :chooseDate AND a.appointmentHour = :appointmentHour")
    boolean existsByDoctorAndChooseDateAndAppointmentHour(@Param("doctor") User doctor,
                                                          @Param("chooseDate") String chooseDate,
                                                          @Param("appointmentHour") String appointmentHour);

    List<Appointment> findByDoctorUsername(String username);


}
