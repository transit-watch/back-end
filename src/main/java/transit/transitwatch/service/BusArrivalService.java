package transit.transitwatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.arrival.ItemArrival;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class BusArrivalService {

    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;
    @Value("${app.api.key.sbus}")
    private String serviceKey;
    private static final Logger logger = LoggerFactory.getLogger(BusStopCrowdingService.class);

    /*
     * 버스 도착시간 & 혼잡도 api 조회
     * */
    public CommonApiDTO<ItemArrival> getBusArrivalInformationApi(String stId, String busRouteId, int ord) throws Exception {

        URI url = getApiUrl(stId, busRouteId, ord);
        String apiResult;

        // api 받아오기
        try {
            apiResult = apiUtil.getApiUri(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // api 결과 dto에 넣기
        CommonApiDTO<ItemArrival> commonApiDTO = apiJsonParser.busGoKrParser(apiResult, new TypeReference<>() {});

        return commonApiDTO;
    }

    // Url 가져오기
    public URI getApiUrl(String stId, String busRouteId, int ord) {
        String url = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute" +
                "?stId=" + stId +
                "&busRouteId=" + busRouteId +
                "&ord=" + ord +
                "&resultType=json" +
                "&serviceKey=" + serviceKey;
        return apiUtil.getConvertUri(url);
    }
}
