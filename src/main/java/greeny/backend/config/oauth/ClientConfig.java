package greeny.backend.config.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {  // Rest API 요청 메소드를 사용하기 위해 Bean 등록
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
