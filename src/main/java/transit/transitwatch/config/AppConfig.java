package transit.transitwatch.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * RESTful 서비스 호출을 위한 {@code RestTemplate} 빈을 생성합니다.
     *
     * @return RESTful 서비스 호출에 사용될 {@code RestTemplate} 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PersistenceContext
    private EntityManager em;

    /**
     * JPAQueryFactory를 생성하여 QueryDSL을 사용할 수 있도록 설정한다.
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
}
