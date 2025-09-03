package finalmission.reservation.domain;

import finalmission.member.domain.MemberFixture;
import finalmission.room.domain.RoomFixture;
import finalmission.time.domain.TimeFixture;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationFixture {

    private static final AtomicLong identifier = new AtomicLong(1L);

    public static Reservation create() {
        long id = identifier.getAndIncrement();
        return new Reservation(
                id,
                RoomFixture.create(),
                LocalDate.now().plusDays(id),
                TimeFixture.create(),
                MemberFixture.create()
        );
    }

    public static Reservation createWithoutId() {
        long id = identifier.getAndIncrement();
        return new Reservation(
                RoomFixture.create(),
                LocalDate.now().plusDays(id),
                TimeFixture.create(),
                MemberFixture.create()
        );
    }
}
