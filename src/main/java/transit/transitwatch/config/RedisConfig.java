package transit.transitwatch.config;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfig {
    private final RedisProperties redisProperties;

    /**
     * Lettuce로 Redis 연결 팩토리를 설정한다.
     *
     * @return Redis 연결용 LettuceConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(redisProperties.getHost());
        redisConfiguration.setPort(redisProperties.getPort());
        redisConfiguration.setPassword(redisProperties.getPassword());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);

        return lettuceConnectionFactory;
    }

    /**
     * Redis 작업을 위한 RedisTemplate을 설정한다.
     * 이 메서드에서는 Redis 연결을 위해 RedisTemplate을 만들고, 키랑 값, 해시 키와 값의 직렬화 방식을 설정해준다.
     *
     * @return 설정 된 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    /**
     * RediSearch 이용을 위한 Redis Modules를 지원하는 연결을 생성하여 반환한다.
     *
     * @return Redis Modules 연결
     */
    @Bean
    public StatefulRedisModulesConnection<String, String> redisModulesConnection() {
        RedisModulesClient client = RedisModulesClient.create("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());

        return client.connect();
    }
}
