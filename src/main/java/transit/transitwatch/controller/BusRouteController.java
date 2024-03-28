package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.service.BusRouteService;

@RequiredArgsConstructor
@RestController
public class BusRouteController {

    private final BusRouteService busRouteService;

    /*
     * 버스 노선 경로 파일 다운로드 + 엑셀을 csv로 변환 + 저장
     * */
    @PostMapping("/bus-stops/route")
    public void downloadAndSaveBusRoute() {
        // csv파일 db에 저장하기
        try {
            busRouteService.saveBusRouteFile("bus_route.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
