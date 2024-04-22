package transit.transitwatch.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.CreateOptions;
import com.redis.lettucemod.search.Field;
import com.redis.lettucemod.search.SearchOptions;
import com.redis.lettucemod.search.SearchResults;
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
import transit.transitwatch.dto.SearchKeywordDTO;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class AutocompleteServiceTest {

    //    @Mock
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    StatefulRedisModulesConnection<String, String> redisModulesConnection;
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

    @DisplayName("좌표 기반 정렬 자동완성 테스트")
    @Test
    void autoGeo() throws Exception {
        String[] split = "시청.서소문청사|02135".split("\\|");
        System.out.println(split[1]);
    }

    @DisplayName("redisearch 테스트")
    @Test
    void createIndex() {
        RedisModulesCommands<String, String> commands = redisModulesConnection.sync();
        /*
         * ON <DataType>: 인덱싱할 데이터 타입을 지정합니다. 대부분의 경우 HASH가 사용됩니다.
         * PREFIX <count> <prefix>: 인덱싱할 키의 접두사를 지정합니다. <count>는 접두사가 적용될 키의 개수를 의미하며, <prefix>는 해당 키의 접두사입니다.
         * */
        //FT.CREATE autoindex ON HASH PREFIX 1 autocomplete: SCHEMA stationId TAG SORTABLE stationName TEXT WEIGHT 5.0 SORTABLE arsId TAG SORTABLE location GEO
        commands.ftCreate("autoindex4",
                CreateOptions.<String, String>builder()
                        .on(CreateOptions.DataType.HASH)
                        .prefix("autocomplete:").build(),
                Field.tag("stationId").sortable().build(),
                Field.text("stationName").sortable().build(),
                Field.tag("arsId").sortable().build(),
                Field.text("xLatitude").sortable().build(),
                Field.text("yLongitude").sortable().build()
        );

        String[] stationNames = {"Central Station", "East Station", "West Station", "North Station", "South Station",
                "Park Station", "Lake Station", "Hill Station", "River Station", "Forest Station"};
        String[] arsIds = {"1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008", "1009", "1010"};
        double[] latitudes = {34.052235, 34.052236, 34.052237, 34.052238, 34.052239,
                34.052240, 34.052241, 34.052242, 34.052243, 34.052244};
        double[] longitudes = {-118.243683, -118.243684, -118.243685, -118.243686, -118.243687,
                -118.243688, -118.243689, -118.243690, -118.243691, -118.243692};

        for (int i = 0; i < 10; i++) {
            String key = "autocomplete:" + (i + 1);
            Map<String, String> data = new HashMap<>();
            data.put("stationId", "ST00" + (i + 1));
            data.put("stationName", stationNames[i]);
            data.put("arsId", arsIds[i]);
            data.put("xLatitude", String.valueOf(latitudes[i]));
            data.put("yLongitude", String.valueOf(longitudes[i]));
            commands.hmset(key, data);
        }
    }

    @DisplayName("redisearch 조회 테스트")
    @Test
    void search() {
        RedisModulesCommands<String, String> commands = redisModulesConnection.sync();
        String query="111111111|119000285|119900006|119000320|119000048|119900320|119000302|119000080|121900214|119000084|119000083|119900209|121000473|277101688|121001325|119000319|119900083|119900312|119000296|119900037|121900044|121900180|119900055|219000097|119900277|121900204|219000072|120000059|120000672|121900009|120000676|120000677|121000165|121000166|121001314|121001315|119900192";
        //FT.SEARCH autoindex "@stationId:{119000285|119900006|119000320|119000048|119900320}
        SearchResults<String, String> results = commands.ftSearch("autoindex", "@stationId:{"+query+"}",
                SearchOptions.<String, String>builder()
                        .returnFields("stationId", "stationName", "arsId", "xLatitude", "yLongitude","nextStationName")
                        .sortBy(SearchOptions.SortBy.asc("stationName"))
                        .build()
        );
        System.out.println("results = " + results);
        List<SearchKeywordDTO> busStops = new ArrayList<>();
        results.forEach(result -> {
            String stationId = result.get("stationId");
            String stationName = result.get("stationName");
            String arsId = result.get("arsId");
            String latitude = result.get("xLatitude");
            String longitude = result.get("yLongitude");
            String nextStationName = result.get("nextStationName");


            busStops.add(new SearchKeywordDTO(stationId, stationName, arsId, latitude, longitude,nextStationName));
        });

        // 거리 기준 정렬
        busStops.sort(Comparator.comparingDouble(a -> a.distance("126.95584930", "37.53843986")));


//        System.out.println("documents = " + results);
        System.out.println("busStops = " + busStops);
    }


}