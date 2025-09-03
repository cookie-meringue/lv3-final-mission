package finalmission.time.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import finalmission.time.domain.Time;
import finalmission.time.domain.TimeFixture;
import finalmission.time.repository.TimeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {TimeService.class, TimeRepository.class})
class TimeServiceTest {

    @Autowired
    private TimeService timeService;

    @MockitoBean
    private TimeRepository timeRepository;

    @Test
    void id_를_통해_조회할_수_있다() {
        // given
        final Time time = TimeFixture.create();
        final Optional<Time> expected = Optional.of(time);
        when(timeRepository.findById(time.getId()))
                .thenReturn(Optional.of(time));

        // when
        final Optional<Time> actual = timeService.findById(time.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
