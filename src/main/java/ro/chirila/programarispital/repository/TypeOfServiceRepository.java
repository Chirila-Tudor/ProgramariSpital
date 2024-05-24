package ro.chirila.programarispital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.chirila.programarispital.repository.entity.TypeOfService;

@Repository
public interface TypeOfServiceRepository extends JpaRepository<TypeOfService, Long> {
    TypeOfService findByService(String service);
}
