package finalmission.time.domain;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

public class TimeFixture {

    private static final AtomicLong identifier = new AtomicLong(1L);

    public static Time create() {
        long id = identifier.getAndIncrement();
        return new Time(
                id,
                LocalTime.now().plusMinutes(id)
        );
    }

    public static Time createWithoutId() {
        long id = identifier.getAndIncrement();
        return new Time(
                LocalTime.now().plusMinutes(id)
        );
    }
}
