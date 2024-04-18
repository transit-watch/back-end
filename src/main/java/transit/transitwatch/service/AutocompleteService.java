package transit.transitwatch.service;


import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.CreateOptions;
import com.redis.lettucemod.search.Field;
import com.redis.lettucemod.search.SearchOptions;
import com.redis.lettucemod.search.SearchResults;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.SearchKeywordDTO;
import transit.transitwatch.exception.ServiceException;
import transit.transitwatch.repository.BusStopInfoRepository;
import transit.transitwatch.repository.projections.SearchKeywordProjection;
import transit.transitwatch.util.TrieNode;

import java.util.*;

import static transit.transitwatch.util.ErrorCode.AUTOCOMPLETE_BASEDATA_FAIL;
import static transit.transitwatch.util.ErrorCode.AUTOCOMPLETE_FAIL;

/**
 * Redis 및 MySQL을 사용하여 자동완성 데이터를 색인, 로드 및 검색하는 기능을 관리하는 서비스 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AutocompleteService {

    private final RedisTemplate<String, String> redisTemplate;
    private final BusStopInfoRepository busStopInfoRepository;
    private final RedisModulesCommands<String, String> commands;
    private final TrieService trieService;

    /**
     * 주어진 키워드에 대한 자동완성 결과를 반환한다.
     *
     * @param keyword 사용자가 입력한 검색 키워드
     * @return 자동완성된 버스 정류장 이름 목록
     * @throws ServiceException 자동완성 검색 실패 시 발생
     */
    public Set<String> autocomplete(String keyword) {
        try {
            TrieNode trie = trieService.getTrie();
            return trie.autocomplete(keyword);
        } catch (Exception e) {
            log.error("자동완성 검색어 로딩 중 오류가 발생했습니다. 키워드 : {}", keyword, e);
            throw new ServiceException(AUTOCOMPLETE_FAIL);
        }
    }

    /**
     * Redis에서 사용할 자동완성 인덱스를 생성한다.
     *
     * @throws ServiceException 인덱스 생성 실패 시 발생
     */
    public void createIndex() {
        try {
            commands.ftInfo("autoindex");
        } catch (RedisCommandExecutionException e) {
            log.error("Redis 인덱스 정보를 검색하지 못했습니다.", e);
            //FT.CREATE autoindex ON HASH PREFIX 1 autocomplete: SCHEMA stationId TAG SORTABLE stationName TEXT WEIGHT 5.0 SORTABLE arsId TAG SORTABLE location GEO
            if (e.getMessage().contains("Unknown index name")) {
                try {
                    commands.ftCreate("autoindex",
                            CreateOptions.<String, String>builder()
                                    .on(CreateOptions.DataType.HASH)
                                    .prefix("autocomplete:").build(),
                            Field.tag("stationId").sortable().build(),
                            Field.text("stationName").sortable().build(),
                            Field.tag("arsId").sortable().build(),
                            Field.text("xLatitude").sortable().build(),
                            Field.text("yLongitude").sortable().build()
                    );
                } catch (RedisCommandExecutionException ce) {
                    log.error("Redis 인덱스 생성 중 오류가 발생했습니다.", ce);
                    throw new ServiceException(AUTOCOMPLETE_BASEDATA_FAIL);
                }
            }
        }
    }

    /**
     * MySQL에서 버스 정류장 정보를 가져와 Redis에 저장한다.
     *
     * @throws ServiceException 데이터 로딩 실패 시 발생
     */
    public void loadBusStopInfoMysqlToRedis() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        List<SearchKeywordProjection> all = busStopInfoRepository.selectBusStopInfo();
        try {
            all.forEach(busStop -> {
                // hash에 정류장명, 경도, 위도, ars_id 전부 저장하기
                Map<String, String> data = new HashMap<>();
                data.put("stationId", busStop.getStationId());
                data.put("stationName", busStop.getStationName());
                data.put("arsId", busStop.getArsId());
                data.put("xLatitude", String.valueOf(busStop.getXLatitude()));
                data.put("yLongitude", String.valueOf(busStop.getYLongitude()));
                data.put("nextStationName", busStop.getNextStationName());

                hashOperations.putAll("autocomplete:" + busStop.getStationId(), data);
            });
        } catch (Exception e) {
            log.error("Mysql -> Redis로 버스 정류장 정보 만드는 중 오류가 발생했습니다.", e);
            throw new ServiceException(AUTOCOMPLETE_BASEDATA_FAIL);
        }
    }
    
    /**
     * 주어진 자동완성 목록과 좌표를 기반으로 검색 결과를 정렬한다.
     *
     * @param autocomplete 자동완성된 키워드 집합
     * @param xLatitude 사용자의 위도
     * @param yLongitude 사용자의 경도
     * @return 정렬된 검색 키워드 DTO 목록
     * @throws ServiceException 검색 실패 시 발생
     */
    public List<SearchKeywordDTO> searchAndSort(Set<String> autocomplete, String xLatitude, String yLongitude) {
        // FT.SEARCH autoindex "@stationId:{1010001|010101010}"
        StringBuilder query = new StringBuilder();
        try {
            autocomplete.forEach(a -> {
                if (!query.isEmpty()) query.append("|");
                query.append(a);
            });
            log.debug("쿼리에 들어갈 정류장 정보: {}", query);

            SearchResults<String, String> results = commands.ftSearch("autoindex", "@stationId:{" + query + "}",
                    SearchOptions.<String, String>builder()
                            .returnFields("stationId", "stationName", "arsId", "xLatitude", "yLongitude", "nextStationName")
                            .sortBy(SearchOptions.SortBy.asc("stationName"))
                            .build()
            );
            log.debug("검색 결과: {}", results);

            return sortLocation(results, xLatitude, yLongitude);
        } catch (RedisCommandExecutionException e) {
            log.error("Redis에서 검색 작업 중 오류가 발생했습니다.", e);
            throw new ServiceException(AUTOCOMPLETE_FAIL);
        } catch (Exception e) {
            log.error("검색, 정렬 작업 중 오류가 발생했습니다.", e);
            throw new ServiceException(AUTOCOMPLETE_FAIL);
        }
    }
    
    /**
     * 결과 목록을 사용자 위치를 기준으로 정렬한다.
     *
     * @param results 검색 결과
     * @param xLatitude 사용자의 위도
     * @param yLongitude 사용자의 경도
     * @return 거리에 따라 정렬된 버스 정류장 목록
     * @throws ServiceException 정렬 실패 시 발생
     */
    private List<SearchKeywordDTO> sortLocation(SearchResults<String, String> results, String xLatitude, String yLongitude) {
        List<SearchKeywordDTO> busStops = new ArrayList<>();
        try {
            results.forEach(result -> {
                String stationId = result.get("stationId");
                String stationName = result.get("stationName");
                String arsId = result.get("arsId");
                String latitude = result.get("xLatitude");
                String longitude = result.get("yLongitude");
                String nextStationName = result.get("nextStationName");

                busStops.add(new SearchKeywordDTO(stationId, stationName, arsId, latitude, longitude, nextStationName));
            });

        busStops.sort(Comparator.comparingDouble(a -> a.distance(xLatitude, yLongitude)));
        log.debug("현위치에 가까운 버스 정류장 정렬: {}", busStops);
        } catch (Exception e) {
            log.error("정류장 목록 정렬 중 오류가 발생했습니다.", e);
            throw new ServiceException(AUTOCOMPLETE_FAIL);
        }
        return busStops.size() > 10 ? busStops.subList(0, 10) : busStops;
    }
}
