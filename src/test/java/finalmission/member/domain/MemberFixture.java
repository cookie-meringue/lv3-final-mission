package finalmission.member.domain;

import java.util.concurrent.atomic.AtomicLong;

public class MemberFixture {

    private static final AtomicLong identifier = new AtomicLong(1L);

    public static Member create() {
        long id = identifier.getAndIncrement();
        return new Member(
                id,
                "testUser" + id,
                id + "testEmail@naver.com",
                "testPassword" + id
        );
    }

    public static Member createWithoutId() {
        long id = identifier.getAndIncrement();
        return new Member(
                "testUser" + id,
                id + "testEmail@naver.com",
                "testPassword" + id
        );
    }
}
