package finalmission.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import finalmission.room.domain.Room;
import finalmission.room.domain.RoomFixture;
import finalmission.time.domain.Time;
import finalmission.time.domain.TimeFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void 특정_멤버의_예약인지_확인한다() {
        // given
        final Member member = MemberFixture.create();
        final Room room = RoomFixture.create();
        final Time time = TimeFixture.create();
        final Reservation reservation = new Reservation(
                room,
                LocalDate.now(),
                time,
                member
        );
        final boolean expected = true;

        // when
        final boolean actual = reservation.isReservedBy(member);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 날짜를_변경한다() {
        // given
        final Member member = MemberFixture.create();
        final Room room = RoomFixture.create();
        final Time time = TimeFixture.create();
        final Reservation reservation = new Reservation(
                room,
                LocalDate.now(),
                time,
                member
        );
        final LocalDate dateToUpdate = LocalDate.now().plusDays(20);

        // when
        reservation.updateDate(dateToUpdate);

        // then
        assertThat(reservation.getDate()).isEqualTo(dateToUpdate);
    }

    @Test
    void 시간을_변경한다() {
        // given
        final Member member = MemberFixture.create();
        final Room room = RoomFixture.create();
        final Time time = TimeFixture.create();
        final Reservation reservation = new Reservation(
                room,
                LocalDate.now(),
                time,
                member
        );
        final Time timeToUpdate = TimeFixture.create();

        // when
        reservation.updateTime(timeToUpdate);

        // then
        assertThat(reservation.getTime()).isEqualTo(timeToUpdate);
    }
}
