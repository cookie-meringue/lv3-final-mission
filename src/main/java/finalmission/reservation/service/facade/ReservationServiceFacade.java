package finalmission.reservation.service.facade;

import finalmission.auth.infrastructure.methodargument.MemberPrincipal;
import finalmission.exception.domain.BadRequestException;
import finalmission.exception.domain.ConflictException;
import finalmission.exception.domain.ForbiddenException;
import finalmission.member.domain.Member;
import finalmission.member.service.MemberService;
import finalmission.reservation.domain.Reservation;
import finalmission.reservation.dto.request.ReservationCreationRequest;
import finalmission.reservation.dto.request.ReservationUpdateRequest;
import finalmission.reservation.dto.response.ReservationCreationResponse;
import finalmission.reservation.dto.response.ReservationResponse;
import finalmission.reservation.service.ReservationService;
import finalmission.room.domain.Room;
import finalmission.room.service.RoomService;
import finalmission.time.domain.Time;
import finalmission.time.service.TimeService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationServiceFacade {


    private final TimeService timeService;
    private final RoomService roomService;
    private final MemberService memberService;
    private final ReservationService reservationService;

    @Transactional
    public ReservationCreationResponse create(
            final ReservationCreationRequest request,
            final MemberPrincipal memberPrincipal
    ) {

        final Room room = roomService.findById(request.roomId())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 회의실입니다."));
        final Time time = timeService.findById(request.timeId())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 시간입니다."));
        final Member member = memberService.findByPrincipal(memberPrincipal)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        final Reservation reservation = reservationService.create(
                room,
                request.date(),
                time,
                member
        );

        return ReservationCreationResponse.fromReservation(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        final List<Reservation> reservations = reservationService.findAll();

        return reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findByMemberPrincipal(final MemberPrincipal memberPrincipal) {
        final Member member = memberService.findByPrincipal(memberPrincipal)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        final List<Reservation> reservations = reservationService.findAllByMember(member);

        return reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }

    @Transactional
    public void delete(final Long id, final MemberPrincipal memberPrincipal) {
        final Member member = memberService.findByPrincipal(memberPrincipal)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        final Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 예약입니다."));
        validateReservationOwner(reservation, member);

        reservationService.delete(reservation);
    }

    @Transactional
    public void update(
            final Long id,
            final ReservationUpdateRequest request,
            final MemberPrincipal memberPrincipal
    ) {
        final Member member = memberService.findByPrincipal(memberPrincipal)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        final Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 예약입니다."));
        final Time time = timeService.findById(request.timeId())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 시간입니다."));
        validateReservationOwner(reservation, member);

        if (reservationService.existsByRoomAndDateAndTime(reservation.getRoom(), request.date(), time)) {
            throw new ConflictException("이 회의실은 해당 날짜와 시간에 이미 예약이 존재합니다.");
        }

        reservation.updateDate(request.date());
        reservation.updateTime(time);
    }

    private void validateReservationOwner(final Reservation reservation, final Member member) {
        if (!reservation.isReservedBy(member)) {
            throw new ForbiddenException("권한이 없습니다.");
        }
    }
}
