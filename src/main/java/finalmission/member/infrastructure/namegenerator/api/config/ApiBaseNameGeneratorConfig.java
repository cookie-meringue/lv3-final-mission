package finalmission.member.infrastructure.namegenerator.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApiBaseNameGeneratorConfig {

    private static final String API_URL = "https://randommer.io/api/Name";
    private static final String API_KEY_NAME = "X-Api-Key";
    private final String apiKey;

    public ApiBaseNameGeneratorConfig(@Value("${api.randommer.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Bean
    public RestClient apiBaseNameGeneratorRestClient() {
        return RestClient.builder()
                .baseUrl(API_URL)
                .defaultHeader(API_KEY_NAME, apiKey)
                .build();
    }
}
