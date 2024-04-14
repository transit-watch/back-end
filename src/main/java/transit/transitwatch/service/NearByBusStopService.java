package transit.transitwatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.near.ItemNear;
import transit.transitwatch.dto.response.NearByBusStopResponse;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.repository.BusStopInfoRepository;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NearByBusStopService {

    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;
    @Value("${app.api.key.sbus}")
    private String serviceKey;
    private final BusStopCrowdingService busStopCrowdingService;
    private final BusStopInfoRepository busStopInfoRepository;

    private static final Logger logger = LoggerFactory.getLogger(BusStopCrowdingService.class);

    /*
     * 좌표기반 근처 버스 정류장 목록 조회
     * */
    public CommonApiDTO<ItemNear> getNearByBusStopApi(double tmX, double tmY, int radius) throws Exception {
        URI url = getApiUrl(tmX, tmY, radius);
        String apiResult;

        // api 받아오기
        try {
            apiResult = apiUtil.getApiUri(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // api 결과 dto에 넣기
        CommonApiDTO<ItemNear> commonApiDTO = apiJsonParser.busGoKrParser(apiResult, new TypeReference<>() {});

        return commonApiDTO;
    }

    // Url 가져오기
    private URI getApiUrl(double tmX, double tmY, int radius) {
        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos" +
                "?tmX=" + tmX +
                "&tmY=" + tmY +
                "&radius=" + radius +
                "&resultType=json" +
                "&serviceKey=" + serviceKey;
        return apiUtil.getConvertUri(url);
    }

    /*
     * 좌표기반 근처 버스 정류장 목록 response객체로 만들기
     * */
    public List<NearByBusStopResponse> getNearByBusStopResponses(CommonApiDTO<ItemNear> commonApiDTO) {
        // 가져온 데이터에 정류소의 혼잡도 정보 추가해서 response
        List<NearByBusStopResponse> collect = commonApiDTO.getMsgBody().getItemList().stream()
                .map(item -> {
                    ItisCdEnum itisCdEnum = null;
                    try {
                        // 해당 정류장의 혼잡도 정보 가져오기
                        itisCdEnum = busStopCrowdingService.selectBusStopCrowding(item.getArsId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    // 정류장 명 가져오기
                    BusStopInfo busStopInfo = busStopInfoRepository.findByArsId(item.getArsId());

                    // dto에 set
                    return NearByBusStopResponse.builder()
                            .stationId(item.getStationId())
                            .stationName(busStopInfo.getStationName())
                            .arsId(item.getArsId())
                            .xLatitude(Double.parseDouble(item.getGpsX()))
                            .yLongitude(Double.parseDouble(item.getGpsY()))
                            .distance(Integer.parseInt(item.getDist()))
                            .crowding(itisCdEnum.name())
                            .build();

                })
                .collect(Collectors.toList());
        return collect;
    }
}
