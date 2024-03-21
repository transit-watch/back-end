package transit.transitwatch.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;

    /*
     * QueryDsl설정
     * */
    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
}
