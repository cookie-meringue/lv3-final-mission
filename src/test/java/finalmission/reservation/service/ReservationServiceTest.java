package finalmission.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import finalmission.reservation.domain.Reservation;
import finalmission.reservation.domain.ReservationFixture;
import finalmission.reservation.repository.ReservationRepository;
import finalmission.room.domain.Room;
import finalmission.room.domain.RoomFixture;
import finalmission.time.domain.Time;
import finalmission.time.domain.TimeFixture;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {ReservationService.class, ReservationRepository.class})
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockitoBean
    private ReservationRepository reservationRepository;

    @Test
    void 예약을_생성할_수_있다() {
        // given
        final Room room = RoomFixture.create();
        final LocalDate date = LocalDate.now();
        final Time time = TimeFixture.create();
        final Member member = MemberFixture.create();
        final Reservation expected = Reservation.makeReservation(
                room,
                date,
                time,
                member
        );

        when(reservationRepository.save(expected))
                .thenReturn(expected);

        // when
        final Reservation actual = reservationService.create(
                room,
                date,
                time,
                member
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 전부_조회할_수_있다() {
        // given
        final List<Reservation> reservations = IntStream.range(0, 10)
                .mapToObj(i -> ReservationFixture.create())
                .toList();
        when(reservationRepository.findAll())
                .thenReturn(reservations);

        // when
        final List<Reservation> actual = reservationService.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(reservations);
    }

    @Test
    void 멤버를_통해_조회할_수_있다() {
        // given
        final Member member = MemberFixture.create();
        final List<Reservation> memberReservations = IntStream.range(0, 10)
                .mapToObj(i -> ReservationFixture.create())
                .toList();
        when(reservationRepository.findAllByMember(member))
                .thenReturn(memberReservations);

        // when
        final List<Reservation> actual = reservationService.findAllByMember(member);

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(memberReservations);
    }

    @Test
    void id_를_통해_조회할_수_있다() {
        // given
        final Long id = 1L;
        final Reservation found = ReservationFixture.create();
        final Optional<Reservation> expected = Optional.of(found);
        when(reservationService.findById(id))
                .thenReturn(expected);

        // when
        final Optional<Reservation> actual = reservationService.findById(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 삭제할_수_있다() {
        // given
        final Reservation reservation = ReservationFixture.create();

        // when
        reservationService.delete(reservation);

        // then
        verify(reservationRepository).delete(reservation);
    }

    @Test
    void 회의실과_날짜와_시간으로_존재_여부를_판단할_수_있다() {
        // given
        final Reservation reservation = ReservationFixture.create();
        final boolean expected = true;
        when(
                reservationRepository.existsByRoomAndDateAndTime(
                        reservation.getRoom(),
                        reservation.getDate(),
                        reservation.getTime()
                )
        ).thenReturn(expected);

        // when
        final boolean actual = reservationService.existsByRoomAndDateAndTime(
                reservation.getRoom(),
                reservation.getDate(),
                reservation.getTime()
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
