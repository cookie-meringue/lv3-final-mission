package finalmission.reservation.repository;

import finalmission.member.domain.Member;
import finalmission.reservation.domain.Reservation;
import finalmission.room.domain.Room;
import finalmission.time.domain.Time;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByMember(final Member member);

    boolean existsByRoomAndDateAndTime(final Room room, final LocalDate date, final Time time);
}
