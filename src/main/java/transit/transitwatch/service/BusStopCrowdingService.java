package transit.transitwatch.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.repository.BusStopCrowdingRepository;
import transit.transitwatch.repository.BusStopCrowdingViewRepository;
import transit.transitwatch.repository.projections.BusStopCrowdingProjection;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BusStopCrowdingService {

    private final BusStopCrowdingViewRepository busStopCrowdingViewRepository;
    private final BusStopCrowdingRepository busStopCrowdingRepository;

    private final ApiJsonParser apiJsonParser;
    private final ApiUtil apiUtil;

    @Value("${app.api.key.tdata}")
    private String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(BusStopCrowdingService.class);

    /*
     * 버스 정류장 혼잡도 list 저장하기
     * */
    public void saveBusStopCrowdingApi(int pageNo, int numOfRows) throws Exception {
        // Url 가져오기
        String url = getApiUrl(pageNo, numOfRows);
        String apiResult;

        // api 받아오기
        try {
            apiResult = apiUtil.getApi(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // api 결과 dto에 넣기
        List<BusStopCrowdingDTO> busStopCrowdingDTOList = apiJsonParser.busStopCrowdingParser(apiResult);

        // db에 저장하기
        try {
            List<BusStopCrowding> busStopCrowdingList = busStopCrowdingDTOList.stream()
                    .map(BusStopCrowdingDTO::toEntity)
                    .collect(Collectors.toList());
            busStopCrowdingRepository.saveAll(busStopCrowdingList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Url 가져오기
    private String getApiUrl(int pageNo, int numOfRows) {
        String url = "https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xBusStationCrowdedInformation/1.0" +
                "?pageNo=" + pageNo +
                "&numOfRows=" + numOfRows +
                "&apikey=" + apiKey;
        return url;
    }

    /*
     * 버스 정류장 혼잡도 조회
     * */
    public ItisCdEnum selectBusStopCrowding(String arsId) throws Exception {

        return busStopCrowdingViewRepository.findByArsId(arsId)
                .map(BusStopCrowdingProjection::getItisCd)
                .map(itisCd -> {
                    // itisCd(혼잡여부 코드)랑 일치하는 enum 반환. 없으면 여유 반환
                    for (ItisCdEnum itisCdEnum : ItisCdEnum.values()) {
                        if (itisCd.equals(itisCdEnum.getCode())) {
                            return itisCdEnum;
                        }
                    }
                    return null;
                })
                .orElse(ItisCdEnum.EASYGOING);
    }
}
