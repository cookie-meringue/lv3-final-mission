package finalmission.reservation.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.auth.infrastructure.methodargument.MemberPrincipal;
import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import finalmission.member.service.MemberService;
import finalmission.reservation.domain.Reservation;
import finalmission.reservation.domain.ReservationFixture;
import finalmission.reservation.dto.request.ReservationCreationRequest;
import finalmission.reservation.dto.request.ReservationUpdateRequest;
import finalmission.reservation.dto.response.ReservationCreationResponse;
import finalmission.reservation.dto.response.ReservationResponse;
import finalmission.reservation.service.ReservationService;
import finalmission.room.domain.Room;
import finalmission.room.domain.RoomFixture;
import finalmission.room.service.RoomService;
import finalmission.time.domain.Time;
import finalmission.time.domain.TimeFixture;
import finalmission.time.service.TimeService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReservationServiceFacadeTest {

    @Autowired
    private ReservationServiceFacade reservationServiceFacade;

    @MockitoBean
    private TimeService timeService;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 예약을_생성할_수_있다() {
        // given
        final Member member = MemberFixture.create();
        final Room room = RoomFixture.create();
        final LocalDate date = LocalDate.now();
        final Time time = TimeFixture.create();
        final ReservationCreationRequest request = new ReservationCreationRequest(
                room.getId(),
                date,
                time.getId()
        );
        final MemberPrincipal memberPrincipal = new MemberPrincipal(member.getEmail());
        final Reservation reservation = Reservation.makeReservation(
                room,
                date,
                time,
                member
        );
        when(roomService.findById(request.roomId()))
                .thenReturn(Optional.of(room));
        when(timeService.findById(request.timeId()))
                .thenReturn(Optional.of(time));
        when(memberService.findByPrincipal(memberPrincipal))
                .thenReturn(Optional.of(member));
        when(reservationService.create(room, date, time, member))
                .thenReturn(reservation);

        final ReservationCreationResponse expected = ReservationCreationResponse.fromReservation(reservation);

        // when
        final ReservationCreationResponse actual = reservationServiceFacade.create(request, memberPrincipal);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 모든_예약을_조회할_수_있다() {
        // given
        final List<Reservation> reservations = IntStream.range(0, 10)
                .mapToObj(i -> ReservationFixture.create())
                .toList();
        final List<ReservationResponse> expected = reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
        when(reservationService.findAll())
                .thenReturn(reservations);

        // when
        final List<ReservationResponse> actual = reservationServiceFacade.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void 멤버_인증_정보를_통해_예약을_조회할_수_있다() {
        // given
        final Member member = MemberFixture.create();
        final MemberPrincipal memberPrincipal = new MemberPrincipal(member.getEmail());
        final List<Reservation> reservations = IntStream.range(0, 10)
                .mapToObj(i -> ReservationFixture.create())
                .toList();
        final List<ReservationResponse> expected = reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
        when(memberService.findByPrincipal(memberPrincipal))
                .thenReturn(Optional.of(member));
        when(reservationService.findAllByMember(member))
                .thenReturn(reservations);

        // when
        final List<ReservationResponse> actual = reservationServiceFacade.findByMemberPrincipal(memberPrincipal);

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // given
        final Reservation reservation = ReservationFixture.create();
        final Member member = reservation.getMember();
        final MemberPrincipal memberPrincipal = new MemberPrincipal(member.getEmail());
        when(memberService.findByPrincipal(memberPrincipal))
                .thenReturn(Optional.of(member));
        when(reservationService.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));

        // when
        reservationServiceFacade.delete(reservation.getId(), memberPrincipal);

        // then
        verify(reservationService).delete(reservation);
    }

    @Test
    void 예약을_수정할_수_있다() {
        // given
        final Room room = RoomFixture.create();
        final Member member = MemberFixture.create();
        final LocalDate dateBeforeUpdate = LocalDate.now();
        final Time timeBeforeUpdate = TimeFixture.create();
        final Reservation reservationBeforeUpdate = Reservation.makeReservation(
                room,
                dateBeforeUpdate,
                timeBeforeUpdate,
                member
        );

        final LocalDate dateAfterUpdate = LocalDate.now().plusDays(1);
        final Time timeAfterUpdate = TimeFixture.create();
        final Reservation reservationAfterUpdate = Reservation.makeReservation(
                room,
                dateAfterUpdate,
                timeAfterUpdate,
                member
        );

        final ReservationUpdateRequest request = new ReservationUpdateRequest(
                dateAfterUpdate,
                timeAfterUpdate.getId()
        );
        final MemberPrincipal memberPrincipal = new MemberPrincipal(member.getEmail());

        when(memberService.findByPrincipal(memberPrincipal))
                .thenReturn(Optional.of(member));
        when(reservationService.findById(reservationBeforeUpdate.getId()))
                .thenReturn(Optional.of(reservationBeforeUpdate));
        when(timeService.findById(timeAfterUpdate.getId()))
                .thenReturn(Optional.of(timeAfterUpdate));
        when(reservationService.existsByRoomAndDateAndTime(room, dateAfterUpdate, timeAfterUpdate))
                .thenReturn(false);

        // when
        reservationServiceFacade.update(reservationBeforeUpdate.getId(), request, memberPrincipal);

        // then
        assertThat(reservationBeforeUpdate).isEqualTo(reservationAfterUpdate);
    }
}
