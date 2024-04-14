package transit.transitwatch.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class AutocompleteServiceTest {

//    @Mock
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Mock
    ValueOperations<String, String> valueOperations;
    @Mock
    ZSetOperations<String, String> zSetOperations;

//    @DisplayName("연결 테스트")
//    @Test
//    void string() throws Exception {
//
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//
//        doNothing().when(valueOperations).set(anyString(), anyString());
//
//        when(valueOperations.get("testname")).thenReturn("test2");
//
//        valueOperations.set("testname", "test");
//        valueOperations.set("testname", "test2");
//
//        String testname = valueOperations.get("testname");
//
//        assertThat(testname).isEqualTo("test2");
//    }

    @DisplayName("자동완성 테스트")
    @Test
    void auto() throws Exception {
        redisTemplate.opsForZSet().add("autocomplete", "test", 0);
        redisTemplate.opsForZSet().add("autocomplete", "123", 0);
        redisTemplate.opsForZSet().add("autocomplete", "toast", 0);
        redisTemplate.opsForZSet().add("autocomplete", "apples", 0);
        redisTemplate.opsForZSet().add("autocomplete", "apaaa", 0);
        redisTemplate.opsForZSet().add("autocomplete", "app", 0);

        Set<String> autocomplete = redisTemplate.opsForZSet().range("autocomplete", 0, -1);

        System.out.println("autocomplete = " + autocomplete);

        String key = "autocomplete";

        Range<String> range = Range.closed("app", "app\uffff");

        Limit limit = Limit.limit().count(10);

        Set<String> ap = redisTemplate.opsForZSet().rangeByLex(key, range, limit);

        System.out.println("app = " + ap);
        assertThat(ap).isNotEmpty();

//        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
//
//        String key = "autocomplete";
//        when(zSetOperations.rangeByLex(key, Range.closed("app", "app\uffff"), Limit.limit().count(10)))
//                .thenReturn(Set.of("apple", "app"));
//
//        Set<String> results = redisTemplate.opsForZSet().rangeByLex(key, Range.closed("app", "app\uffff"), Limit.limit().count(10));
//
//        assertThat(results).containsExactlyInAnyOrder("apple", "app");
//        System.out.println("Results: " + results);
    }
}