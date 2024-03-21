package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.service.BusStopCrowdingService;

@RestController
@RequiredArgsConstructor
public class BusStopCrowdingController {
    private final BusStopCrowdingService busStopCrowdingService;

    /*
    * 버스 정류장 혼잡도 api 조회 후 저장하기
    * */
    @GetMapping("/api/save/busStopCrowding")
    public String saveBusStopCrowding() {
        String apiKey = "d1a183b8-ea3c-4190-8778-67fa9b6b41a1";
        String pageNo = "1";
        String numOfRows = "30";
        String url = "https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xBusStationCrowdedInformation/1.0" +
                "?pageNo=" + pageNo +
                "&numOfRows=" + numOfRows +
                "&apikey=" + apiKey;

        String result;

        try {
            result = busStopCrowdingService.getApi(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
