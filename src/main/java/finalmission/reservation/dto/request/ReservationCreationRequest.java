package finalmission.reservation.dto.request;


import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreationRequest(
        @NotNull
        Long roomId,
        @NotNull
        LocalDate date,
        @NotNull
        Long timeId
) {
}
