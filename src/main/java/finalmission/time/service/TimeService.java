package finalmission.time.service;

import finalmission.time.domain.Time;
import finalmission.time.repository.TimeRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeService {

    private final TimeRepository timeRepository;

    public Optional<Time> findById(final Long id) {
        return timeRepository.findById(id);
    }
}
