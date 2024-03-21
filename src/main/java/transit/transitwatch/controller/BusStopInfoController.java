package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.service.BusStopInfoService;

@RequiredArgsConstructor
@RestController
public class BusStopInfoController {

    private final BusStopInfoService busStopInfoService;

    /*
     * 버스 정류장 정보 csv파일 db에 저장하기
     * */
    @GetMapping("/api/save/busStopInfo")
    public void downloadAndSaveBusStopInfo() {
        try {
            busStopInfoService.saveBusStopInfoFile("bus_stop_info.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
