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
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.service.BusStopCrowdingService;
import transit.transitwatch.service.BusStopInfoService;
import transit.transitwatch.service.DetailBusStopService;
import transit.transitwatch.util.ItisCdEnum;

import java.util.List;

/**
 * 버스 정류장 상세 정보 제공 컨트롤러.
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
public class DetailBusStopController {

    private final DetailBusStopService detailBusStopService;
    private final BusStopInfoService busStopInfoService;
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
     * @param arsId 버스 정류장 ID
     * @return 조회된 상세 버스 정류장 정보가 담긴 Response 객체
     * @throws RuntimeException 정류장 정보 조회 또는 혼잡도 조회 중 예외가 발생한 경우
     */
    @GetMapping("/api/v1/bus-stops/detail/{arsId}")
    public Response<DetailBusStopResponse> detailBusStop(@PathVariable("arsId") @Size(min=5, max=5) @Positive @NotNull String arsId) {

        List<RouteInfo> routeInfoList = detailBusStopService.getDetailBusStop(arsId);

        List<ArrivalInfo> arrivalInfoList = detailBusStopService.getArrivalInfo(routeInfoList);

        List<CombineArrivalRoute> combineArrivalRouteList = detailBusStopService.getCombineRoute(routeInfoList, arrivalInfoList);

        BusStopInfo busStopInfo = busStopInfoService.selectBusStopArsId(arsId);

        ItisCdEnum itisCdEnum;

        itisCdEnum = busStopCrowdingService.selectBusStopCrowding(arsId);

        Station station = Station.builder()
                .arsId(arsId)
                .xLatitude(busStopInfo.getXLatitude())
                .yLongitude(busStopInfo.getYLongitude())
                .stationName(busStopInfo.getStationName())
                .nextStationName(routeInfoList.get(0).getDirection())
                .crowding(itisCdEnum.name())
                .build();

        DetailBusStopResponse response = DetailBusStopResponse.builder()
                .station(station)
                .combineArrivalRouteList(combineArrivalRouteList)
                .build();

        return Response.ok(response);
    }
}
