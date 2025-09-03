package finalmission.reservation.controller;

import finalmission.auth.infrastructure.methodargument.AuthorizedMember;
import finalmission.auth.infrastructure.methodargument.MemberPrincipal;
import finalmission.reservation.dto.request.ReservationCreationRequest;
import finalmission.reservation.dto.request.ReservationUpdateRequest;
import finalmission.reservation.dto.response.ReservationCreationResponse;
import finalmission.reservation.dto.response.ReservationResponse;
import finalmission.reservation.service.facade.ReservationServiceFacade;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationController {

    private final ReservationServiceFacade reservationServiceFacade;

    @PostMapping
    public ResponseEntity<ReservationCreationResponse> create(
            @RequestBody @Valid ReservationCreationRequest request,
            @AuthorizedMember MemberPrincipal memberPrincipal
    ) {
        final ReservationCreationResponse response = reservationServiceFacade.create(request, memberPrincipal);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        final List<ReservationResponse> response = reservationServiceFacade.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> findMine(
            @AuthorizedMember final MemberPrincipal memberPrincipal
    ) {
        final List<ReservationResponse> response = reservationServiceFacade.findByMemberPrincipal(memberPrincipal);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "id") final Long id,
            @AuthorizedMember final MemberPrincipal memberPrincipal
    ) {
        reservationServiceFacade.delete(id, memberPrincipal);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid ReservationUpdateRequest request,
            @AuthorizedMember final MemberPrincipal memberPrincipal
    ) {
        reservationServiceFacade.update(id, request, memberPrincipal);
        return ResponseEntity.noContent().build();
    }
}
