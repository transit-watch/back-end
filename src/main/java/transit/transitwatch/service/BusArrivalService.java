package transit.transitwatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.arrival.ItemArrival;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.exception.ServiceException;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;

import java.net.URI;
import java.net.URISyntaxException;

import static transit.transitwatch.util.ErrorCode.GET_URL_FAIL;

/**
 * API를 통해 특정 버스 정류장과 노선의 도착 시간 및 혼잡도 정보를 조회하는 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BusArrivalService {

    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;
    @Value("${app.api.key.sbus}")
    private String serviceKey;
    private final CacheService cacheService;

    /**
     * 버스 정류장에 정차하는 버스의 도착 시간, 버스 내부 혼잡도 정보를 API로부터 조회한다.
     *
     * @param stId       정류장 ID
     * @param busRouteId 버스 노선 ID
     * @param ord        순서
     * @return 도착 정보를 포함하는 DTO
     */
    @Cacheable(cacheNames = "arrival", key = "#stId + '_' + #busRouteId + '_' + #ord")
    public CommonApiDTO<ItemArrival> getBusArrivalInformationApi(String stId, String busRouteId, int ord) {
        URI url = getApiUrl(stId, busRouteId, ord);

        String apiResult = apiUtil.getApiUri(url);
        return apiJsonParser.busGoKrParser(apiResult, new TypeReference<>() {});
    }
//    public CommonApiDTO<ItemArrival> getBusArrivalInformationApi(String stId, String busRouteId, int ord) {
//        URI url = getApiUrl(stId, busRouteId, ord);
//
//        String apiResult = apiUtil.getApiUri(url);
//        return cacheService.getCache("arrival:" + stId + "_" + busRouteId + "_" + ord,
//                () -> apiJsonParser.busGoKrParser(apiResult, new TypeReference<>() {})
//                , 30, TimeUnit.SECONDS);
//    }



    /**
     * 주어진 정류장, 버스 노선 및 순서에 대한 API URL을 생성한다.
     *
     * @param stId       정류장 ID
     * @param busRouteId 버스 노선 ID
     * @param ord        순서
     * @return 생성된 API URL
     * @throws ServiceException URL 생성 실패 시 발생
     */
    public URI getApiUrl(String stId, String busRouteId, int ord) {
        String url = String.format("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?stId=%s&busRouteId=%s&ord=%d&resultType=json&serviceKey=%s",
                stId, busRouteId, ord, serviceKey);
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            log.error("Url 가져오기에 실패했습니다. Url = {}", url, e);
            throw new ServiceException(e.getMessage(), GET_URL_FAIL);
        }
    }
}
