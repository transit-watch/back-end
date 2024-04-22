package transit.transitwatch.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import transit.transitwatch.dto.response.*;
import transit.transitwatch.entity.BusStopLocation;
import transit.transitwatch.service.BusStopCrowdingService;
import transit.transitwatch.service.BusStopLocationService;
import transit.transitwatch.service.DetailBusStopService;
import transit.transitwatch.util.ItisCdEnum;

import java.util.ArrayList;
import java.util.List;

import static transit.transitwatch.util.ItisCdEnum.NODATA;

/**
 * 버스 정류장 상세 정보 제공 컨트롤러.
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
public class DetailBusStopController {

    private final DetailBusStopService detailBusStopService;
    private final BusStopLocationService busStopLocationService;
    private final BusStopCrowdingService busStopCrowdingService;

    /**
     * 지정된 ARS_ID를 가진 버스 정류장의 상세 정보를 조회한다.
     * <p>이 메서드는 다음 정보를 포함한 정류장의 상세 정보를 반환한다:</p>
     * <ul>
     *     <li>해당 정류장의 버스 노선 목록</li>
     *     <li>각 버스 노선의 도착 예정 정보</li>
     *     <li>정류장의 위치와 이름</li>
     *     <li>정류장의 혼잡도</li>
     * </ul>
     *
     * @param arsId 버스 정류장 ID
     * @return 조회된 상세 버스 정류장 정보가 담긴 Response 객체. 노선 정보가 없는 경우 실패 응답을 반환합니다.
     * @throws RuntimeException 정류장 정보 조회 또는 혼잡도 조회 중 예외가 발생한 경우
     */
    @GetMapping("/api/v1/bus-stops/detail/{arsId}")
    public Response<DetailBusStopResponse> detailBusStop(@PathVariable("arsId") @Size(min = 5, max = 5) @Positive @NotNull String arsId) {
        BusStopLocation location = busStopLocationService.selectBusStopLocation(arsId).orElse(null);
        if (location == null)
            return (Response<DetailBusStopResponse>) Response.fail(NODATA.name());

        List<RouteInfo> routeInfoList = detailBusStopService.getDetailBusStop(arsId);

        List<CombineArrivalRoute> combineArrivalRouteList = new ArrayList<>();
        String direction = "";
        if (!routeInfoList.isEmpty()) {
            List<ArrivalInfo> arrivalInfoList = detailBusStopService.getArrivalInfo(routeInfoList);
            combineArrivalRouteList = detailBusStopService.getCombineRoute(routeInfoList, arrivalInfoList);
            direction = routeInfoList.getFirst().getDirection();
        }
        ItisCdEnum itisCdEnum = busStopCrowdingService.selectBusStopCrowding(arsId);

        Station station = Station.builder()
                .arsId(arsId)
                .yLatitude(location.getYLatitude())
                .xLongitude(location.getXLongitude())
                .stationName(location.getStationName())
                .nextStationName(direction)
                .crowding(itisCdEnum.name())
                .build();

        DetailBusStopResponse response = DetailBusStopResponse.builder()
                .station(station)
                .combineArrivalRouteList(combineArrivalRouteList)
                .build();

        return Response.ok(response);
    }
}
