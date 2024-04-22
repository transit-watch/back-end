package transit.transitwatch.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * 스프링 부트의 캐싱 기능을 활성화하고, Redis를 캐시 저장소로 사용하도록 설정한 클래스.
 */
@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * 모든 캐시의 기본 설정으로 사용할 캐시에 적용된다.
     *
     * @return RedisCacheConfiguration 기본 캐시 구성, 60초의 TTL, null 값 캐싱 비활성화, 키와 값의 직렬화 방법 설정
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * Redis 캐시 매니저로 각기 다른 캐시 구성을 필요로 하는 여러 캐시에 대한 초기 설정을 적용한다.
     * 'busStopLocation', 'busStop', 'detail' 캐시에 동일한 설정을 적용한다.
     *
     * @param connectionFactory Redis 연결 팩토리
     * @return RedisCacheManager Redis 캐시 매니저
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration longLived = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(Map.of(
                        "busStopLocation", longLived,
                        "busStop", longLived,
                        "detail", longLived,
                        "near", longLived
                ))
                .build();
    }
}
