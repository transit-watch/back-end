package transit.transitwatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.response.NearByBusStopResponse;
import transit.transitwatch.service.BusStopCrowdingService;
import transit.transitwatch.service.NearByBusStopService;
import transit.transitwatch.util.ItisCdEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class NearByBusStopController {

    private final NearByBusStopService nearByBusStopService;
    private final BusStopCrowdingService busStopCrowdingService;

    /*
     * 좌표기반 근처 버스 정류장 목록 조회
     * */
    @GetMapping("/bus-stops/near")
    public ResponseEntity<Map<String, Object>> nearByBusStopList(@RequestParam("tmX") double tmX
            , @RequestParam("tmY") double tmY, @RequestParam("radius") int radius) {

        Map<String, Object> response = new HashMap<>();
        CommonApiDTO commonApiDTO = null;

        // api에서 근처 정류장 정보 가져오기
        try {
            commonApiDTO = nearByBusStopService.getNearByBusStopApi(tmX, tmY, radius);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 가져온 데이터에 정류소의 혼잡도 정보 추가해서 response
        List<NearByBusStopResponse> collect = commonApiDTO.getMsgBody().getItemList().stream()
                .map(item -> {
                    ItisCdEnum itisCdEnum = null;
                    try {
                        // 해당 정류장의 혼잡도 정보 가져오기
                         itisCdEnum= busStopCrowdingService.selectBusStopCrowding(item.getArsId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    // dto에 set
                    NearByBusStopResponse input = NearByBusStopResponse.builder()
                            .stationId(item.getStationId())
                            .stationName(item.getStationNm())
                            .arsId(item.getArsId())
                            .xLatitude(Double.parseDouble(item.getGpsX()))
                            .yLongitude(Double.parseDouble(item.getGpsY()))
                            .distance(Integer.parseInt(item.getDist()))
                            .crowding(itisCdEnum.name())
                            .build()
                            ;
                    return input;
                })
                .collect(Collectors.toList());

        response.put("nearByBusStopList", collect);

        return ResponseEntity.ok(response);
    }
}
