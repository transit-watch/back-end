package transit.transitwatch.config;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Value("${spring.data.redis.host}")
    private String getHost;

    /**
     * Lettuce로 Redis 연결 팩토리를 설정한다.
     *
     * @return Redis 연결용 LettuceConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(getHost);
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
     * Redis Modules를 사용하여 Redis 명령을 실행하기 위한 RedisModulesCommands 인스턴스를 생성하고 반환한다.
     *
     * @return RedisModulesCommands 인스턴스.
     */
    @Bean
    public RedisModulesCommands<String, String> redisModulesCommands() {
        String uri = "redis://" + getHost + ":" + redisProperties.getPort();
        log.info("Redisearch connect : {} ", uri);
        RedisModulesClient client = RedisModulesClient.create(uri);
        StatefulRedisModulesConnection<String, String> connection = client.connect();
        RedisModulesCommands<String, String> commands = connection.sync();
        return commands;
    }
}
