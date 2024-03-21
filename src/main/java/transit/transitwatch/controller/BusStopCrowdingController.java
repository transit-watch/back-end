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
    public void saveBusStopCrowding() {
        try {
            busStopCrowdingService.saveBusStopCrowdingApi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 버스 정류장 혼잡도 테이블에서 조회해오기
     * */
    @GetMapping("/api/busStopCrowding")
    public void selectBusStopCrowding() {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        List<BusStopCrowding> resultList = busStopCrowdingService.selectBusStopCrowding(id);
//
//        String result = gson.toJson(resultList);
//
//        return result;
    }
}
