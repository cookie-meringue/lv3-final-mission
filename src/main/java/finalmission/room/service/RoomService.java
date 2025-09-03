package finalmission.room.service;

import finalmission.room.domain.Room;
import finalmission.room.repository.RoomRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomService {

    private final RoomRepository roomRepository;

    public Optional<Room> findById(final Long id) {
        return roomRepository.findById(id);
    }
}
