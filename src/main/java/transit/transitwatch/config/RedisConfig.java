package transit.transitwatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String getHost;

    @Value("${spring.data.redis.password}")
    private String getPassword;

    @Value("${spring.data.redis.port}")
    private int getPort;

    /**
     * Lettuce로 Redis 연결 팩토리를 설정한다.
     *
     * @return Redis 연결용 LettuceConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(getHost);
        redisConfiguration.setPort(getPort);
        redisConfiguration.setPassword(getPassword);
        return new LettuceConnectionFactory(redisConfiguration);
    }

    /**
     * Redis 작업을 위한 RedisTemplate을 설정한다.
     * 이 메서드에서는 Redis 연결을 위해 RedisTemplate을 만들고, 키랑 값, 해시 키와 값의 직렬화 방식을 설정해준다.
     *
     * @return 설정 된 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String, Object> cacheRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

//    @Bean
//    public RedisTemplate<String, Object> cacheRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
//                .builder()
//                .allowIfSubType(Object.class)
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//
//        SimpleModule module = new SimpleModule();
//        // Enum 직렬화 설정
//        module.addSerializer(Enum.class, new JsonSerializer<>() {
//            @Override
//            public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                gen.writeStartObject();
//                gen.writeStringField("@class", value.getDeclaringClass().getName());
//                gen.writeStringField("name", value.name());
//                gen.writeEndObject();
//            }
//        });
//        // Enum 역직렬화 설정
//        module.addDeserializer(Enum.class, new JsonDeserializer<>() {
//            @Override
//            public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
//                JsonNode node = p.getCodec().readTree(p);
//                String className = node.get("@class").asText();
//                String name = node.get("name").asText();
//                try {
//                    Class<?> clazz = Class.forName(className);
//                    if (clazz.isEnum()) {
//                        @SuppressWarnings("unchecked")
//                        Class<? extends Enum> enumClass = (Class<? extends Enum>) clazz;
//                        return Enum.valueOf(enumClass, name);
//                    } else {
//                        throw new IllegalArgumentException("Enum 타입이 아닙니다.");
//                    }
//                } catch (ClassNotFoundException e) {
//                    throw new IOException("Enum 클래스를 찾을 수 없습니다.", e);
//                }
//            }
//        });
//
//        objectMapper.registerModule(module);
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//
//        return template;
//    }

    /**
     * Redis Modules를 사용하여 Redis 명령을 실행하기 위한 RedisModulesCommands 인스턴스를 생성하고 반환한다.
     *
     * @return RedisModulesCommands 인스턴스.
     */
//    @Bean
//    public RedisModulesCommands<String, String> redisModulesCommands() {
//
//        String uri = "redis://" + getHost + ":" + redisProperties.getPort();
//        log.info("--------------redis 모듈커맨드 연결 시작------------- : url = {}", uri);
//        RedisModulesClient client = RedisModulesClient.create(uri);
//        StatefulRedisModulesConnection<String, String> connection = client.connect();
//        RedisModulesCommands<String, String> commands = connection.sync();
//        log.info("--------------redis 모듈커맨드 연결 끝-------------");
//        return commands;
//    }
}