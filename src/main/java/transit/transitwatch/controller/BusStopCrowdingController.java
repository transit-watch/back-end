package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.service.BusStopCrowdingService;

@RequiredArgsConstructor
@RestController
public class BusStopCrowdingController {

    private final BusStopCrowdingService busStopCrowdingService;

    /*
     * 버스 정류장 혼잡도 api 조회 후 저장하기
     * */
    @PostMapping("/bus-stops/crowding")
    public void saveBusStopCrowding(@RequestParam("pageNo") int pageNo, @RequestParam("numOfRows") int numOfRows) {
        try {
            busStopCrowdingService.saveBusStopCrowdingApi(pageNo, numOfRows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
