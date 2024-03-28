package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.repository.DetailBusStopRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DetailBusStopController {
    private final DetailBusStopRepositoryCustom detailBusStopRepositoryCustom;

    /*
     * 버스 정류장 상세정보 - 정류장의 버스 노선 목록 조회해오기
     * */
    @GetMapping("/bus-stops/detail")
    public ResponseEntity<Map<String, Object>> detailBusStop(@RequestParam("arsId") String arsId) {
        Map<String, Object> response = new HashMap<>();

        List<RouteInfo> routeInfoList = detailBusStopRepositoryCustom.searchDetailBusStopList(arsId);

//        response.put("station", );
//        response.put("routeList", );
        return ResponseEntity.ok(response);
    }
}
