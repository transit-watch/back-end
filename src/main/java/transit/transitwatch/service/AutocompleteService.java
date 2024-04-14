package transit.transitwatch.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.repository.BusRouteRepository;
import transit.transitwatch.repository.BusStopInfoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class AutocompleteService {

    private final RedisTemplate<String, String> redisTemplate;
    private final BusStopInfoRepository busStopInfoRepository;
    private final BusRouteRepository busRouteRepository;

    /**
     * MySQL에서 조회한 정류장 정보를 Redis의 자동완성 검색 키에 저장한다.
     *
     * 각 정류장명과 ARS ID를 "autocomplete:station"이라는 ZSet에 저장하고,
     * 추가적으로 각 정류장의 좌표 정보를 별도의 Hash에 저장한다.
     * 이를 통해 자동완성 기능에서 사용할 데이터를 Redis에 미리 적재한다.
     */
    public void loadStationNameMysqlToRedis() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        List<BusStopInfo> all = busStopInfoRepository.findAll();

        all.forEach(keyword ->
                redisTemplate.opsForZSet()
                        .add("autocomplete:station", keyword.getStationName() + "|" + keyword.getArsId(), 0));

        all.forEach(busStopInfo -> {
            String redisKey = "busStopInfo:" + busStopInfo.getArsId();
            Map<String, Object> data = new HashMap<>();
            data.put("yLongitude", busStopInfo.getYLongitude());
            data.put("xLatitude", busStopInfo.getXLatitude());
            hashOperations.putAll(redisKey, data);
        });
    }

//    public void loadRouteNameMysqlToRedis() {
//        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//        List<BusRoute> all = busRouteRepository.findAll();
//        all.forEach(keyword -> redisTemplate.opsForZSet().add("autocomplete:route", keyword.getRouteName() + "|" + keyword.getRouteId(), 0));
//
//        for (BusRoute busRoute : all) {
//            String redisKey = "busRoute:" + busRoute.getArsId();
//            Map<String, Object> data = new HashMap<>();
//
//            hashOperations.putAll(redisKey, data);
//        }
//    }

    /**
     * 자동완성 검색어에 대한 결과를 조회한다.
     *
     * 주어진 prefix를 기반으로 Redis에 저장된 자동완성 검색 키에서
     * 해당 prefix로 시작하는 검색어들을 조회한다. 조회 결과는 최대 10개까지 제한된다.
     *
     * @param prefix 검색어의 접두사
     * @param key 검색 대상이 되는 Redis의 key
     * @return 조회된 검색어 목록
     */
    public Set<String> getAutocomplete(String prefix, String key) {
        String start = prefix;
        String end = prefix + "\uffff";

        Range<String> range = Range.open(start, end);

        Limit limit = Limit.limit().count(10);

        return redisTemplate.opsForZSet().rangeByLex(key, range, limit);

    }
}
