package finalmission.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import finalmission.room.domain.Room;
import finalmission.room.domain.RoomFixture;
import finalmission.room.repository.RoomRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {RoomService.class, RoomRepository.class})
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @MockitoBean
    private RoomRepository roomRepository;

    @Test
    void id_를_통헤_조회할_수_있다() {
        // given
        final Room room = RoomFixture.create();
        final Optional<Room> expected = Optional.of(room);
        when(roomRepository.findById(room.getId()))
                .thenReturn(Optional.of(room));

        // when
        final Optional<Room> actual = roomService.findById(room.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
