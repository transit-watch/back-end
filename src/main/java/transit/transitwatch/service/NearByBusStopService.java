package transit.transitwatch.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NearByBusStopService {
    private final JPAQueryFactory queryFactory;

    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;
    @Value("${app.api.key.sbus}")
    private String serviceKey;
    private static final Logger logger = LoggerFactory.getLogger(BusStopCrowdingService.class);

    /*
     * 좌표기반 근처 버스 정류장 목록 조회
     * */
    public CommonApiDTO getNearByBusStopApi(double tmX, double tmY, int radius) throws Exception {
        URI url = getApiUrl(tmX, tmY, radius);
        String apiResult;

        // api 받아오기
        try {
            apiResult = apiUtil.getApiUri(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // api 결과 dto에 넣기
        CommonApiDTO commonApiDTO = apiJsonParser.nearByBusStopParser(apiResult);

        System.out.println(commonApiDTO);

        return commonApiDTO;
    }

    // Url 가져오기
    private URI getApiUrl(double tmX, double tmY, int radius) {
        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos" +
                "?tmX=" + tmX +
                "&tmY=" + tmY +
                "&radius=" + radius +
                "&resultType=json"  +
                "&serviceKey=" + serviceKey;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }
}
