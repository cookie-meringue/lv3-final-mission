package finalmission.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.member.domain.Member;
import finalmission.reservation.domain.Reservation;
import finalmission.room.domain.Room;
import finalmission.time.domain.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreationResponse(
    Long id,
    RoomResponse room,
    LocalDate date,
    TimeResponse time,
    MemberResponse member
) {

    public static ReservationCreationResponse fromReservation(final Reservation reservation) {
        return new ReservationCreationResponse(
            reservation.getId(),
            RoomResponse.fromRoom(reservation.getRoom()),
            reservation.getDate(),
            TimeResponse.fromTime(reservation.getTime()),
            MemberResponse.fromMember(reservation.getMember())
        );
    }

    public record RoomResponse(
        String name
    ) {

        public static RoomResponse fromRoom(final Room room) {
            return new RoomResponse(room.getName());
        }
    }

    public record TimeResponse(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
    ) {

        public static TimeResponse fromTime(final Time time) {
            return new TimeResponse(time.getStartAt());
        }
    }

    public record MemberResponse(
        String name
    ) {

        public static MemberResponse fromMember(final Member member) {
            return new MemberResponse(member.getName());
        }
    }
}
