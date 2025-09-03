package finalmission.member.infrastructure.namegenerator.api;

import finalmission.member.infrastructure.namegenerator.NameGenerator;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiBaseNameGenerator implements NameGenerator {

    private static final String NAME_TYPE = "surname";
    private static final int QUANTITY = 1;

    private final RestClient apiBaseNameGeneratorRestClient;


    @Override
    public String generate() {
        return Objects.requireNonNull(apiBaseNameGeneratorRestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("nameType", NAME_TYPE)
                        .queryParam("quantity", QUANTITY)
                        .build()
                )
                .retrieve()
                .body(String[].class))[0];
    }
}
