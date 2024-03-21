package transit.transitwatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /*
    * rest template 생성
    * */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
