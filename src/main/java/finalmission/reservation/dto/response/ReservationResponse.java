package finalmission.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.reservation.domain.Reservation;
import finalmission.room.domain.Room;
import finalmission.time.domain.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
        Long id,
        RoomResponse room,
        LocalDate date,
        TimeResponse time
) {

    public static ReservationResponse fromReservation(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                RoomResponse.fromRoom(reservation.getRoom()),
                reservation.getDate(),
                TimeResponse.fromTime(reservation.getTime())
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
}
