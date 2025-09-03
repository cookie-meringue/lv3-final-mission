package finalmission.time.repository;

import finalmission.time.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
