package finalmission.room.domain;

import java.util.concurrent.atomic.AtomicLong;

public class RoomFixture {

    private static final AtomicLong identifier = new AtomicLong(1L);

    public static Room create() {
        long id = identifier.getAndIncrement();
        return new Room(
                id,
                "room" + id
        );
    }

    public static Room createWithoutId() {
        long id = identifier.getAndIncrement();
        return new Room(
                "room" + id
        );
    }
}
