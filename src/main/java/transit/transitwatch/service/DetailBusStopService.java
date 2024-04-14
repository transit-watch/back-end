package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.arrival.ItemArrival;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.response.ArrivalInfo;
import transit.transitwatch.dto.response.CombineArrivalRoute;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.repository.DetailBusStopRepositoryCustom;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static transit.transitwatch.util.ItisCdEnum.NODATA;

@RequiredArgsConstructor
@Service
public class DetailBusStopService {

    private final DetailBusStopRepositoryCustom detailBusStopRepositoryCustom;
    private final BusArrivalService busArrivalService;
    private final ApiUtil apiUtil;

    /**
     * 주어진 ARS ID에 해당하는 모든 노선 정보를 조회한다.
     * <p>해당 정류장의 모든 노선 정보를 조회하여 {@link RouteInfo} 리스트로 반환한다.</p>
     *
     * @param arsId 조회하려는 버스 정류장의 ARS ID
     * @return 해당 정류장의 모든 노선 정보가 담긴 {@link RouteInfo} 리스트
     */
    public List<RouteInfo> getDetailBusStop(String arsId) {
        List<RouteInfo> routeInfoList = detailBusStopRepositoryCustom.searchDetailBusStopList(arsId);

        return routeInfoList;
    }

    /**
     * API로부터 받아온 도착 정보를 {@link ArrivalInfo} 객체에 설정한다.
     * <p>버스의 첫 번째 및 두 번째 도착 정보와 혼잡도 정보를 설정한다.</p>
     *
     * @param item 도착 정보를 포함하고 있는 API 응답 항목
     * @return 설정된 도착 정보가 담긴 {@link ArrivalInfo} 객체
     */
    public ArrivalInfo getArrivalInfo(ItemArrival item) {
        ItisCdEnum crowdingEnum1 = (item.getFirstBusDiv() == 4) ? apiUtil.getCrowdingEnum(item.getFirstBusCrowding()) : NODATA;
        ItisCdEnum crowdingEnum2 = (item.getSecondBusDiv() == 4) ? apiUtil.getCrowdingEnum(item.getSecondBusCrowding()) : NODATA;

        return ArrivalInfo.builder()
                .routeId(item.getBusRouteId())
                .routeType(item.getRouteType())
                .firstArrivalBusCrowding(crowdingEnum1.name())
                .firstArrivalBusTime(item.getFirstBusTime())
                .firstArrivalTimeBefore(apiUtil.getRegex(item.getFirstArrivalMessage()))
                .secondArrivalBusCrowding(crowdingEnum2.name())
                .secondArrivalBusTime(item.getSecondBusTime())
                .secondArrivalTimeBefore(apiUtil.getRegex(item.getSecondArrivalMessage()))
                .build();
    }

    /**
     * 주어진 노선 정보 리스트에 대한 도착 정보를 조회한다.
     * <p>각 노선에 대해 API를 호출하여 도착 정보를 조회하고, 이를 {@link ArrivalInfo} 리스트로 반환한다.</p>
     *
     * @param routeInfoList 조회하려는 노선 정보가 담긴 {@link RouteInfo} 리스트
     * @return 각 노선에 대한 도착 정보가 담긴 {@link ArrivalInfo} 리스트
     * @throws RuntimeException API 호출 중 오류 발생 시
     */
    public List<ArrivalInfo> getArrivalInfo(List<RouteInfo> routeInfoList) {
        List<ArrivalInfo> arrivalInfoList = new ArrayList<>();
        for (RouteInfo busStop : routeInfoList) {
            try { // 각각 버스 노선마다 도착시간 + 혼잡정보 가져오기
                CommonApiDTO<ItemArrival> busArrivalInformationApi = busArrivalService.getBusArrivalInformationApi(busStop.getStationId(), busStop.getRouteId(), busStop.getRouteOrder());
                // 가져온 api중에 아이템 리스트 부분만 가져오기
                List<ItemArrival> itemList = Optional.ofNullable(busArrivalInformationApi.getMsgBody().getItemList()).orElse(Collections.emptyList());
                // 아이템 리스트  arrivalInfo에 넣어주기
                List<ArrivalInfo> tempArrivalInfoList = itemList.stream()
                        .map(this::getArrivalInfo)
                        .collect(Collectors.toList());

                arrivalInfoList.addAll(tempArrivalInfoList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return arrivalInfoList;
    }

    /**
     * 노선 정보와 도착 정보를 결합하여 {@link CombineArrivalRoute} 객체 리스트를 생성한다.
     *
     * <p>노선 정보와 도착 정보를 합쳐, 하나의 노선에 대한 종합적인 정보를 포함하는 {@link CombineArrivalRoute} 객체 리스트를 반환한다.</p>
     *
     * @param routeInfoList   노선 정보가 담긴 {@link RouteInfo} 리스트
     * @param arrivalInfoList 도착 정보가 담긴 {@link ArrivalInfo} 리스트
     * @return 노선 정보와 도착 정보가 결합된 {@link CombineArrivalRoute} 객체 리스트
     */
    public List<CombineArrivalRoute> getCombineRoute(List<RouteInfo> routeInfoList, List<ArrivalInfo> arrivalInfoList) {

        List<CombineArrivalRoute> combineArrivalRouteList = routeInfoList.stream().map( // 노선 정보 가져오기
                routeInfo -> {
                    // routeId가 일치하는 ArrivalInfo 찾기, 없으면 null
                    Optional<ArrivalInfo> matchArrivalInfo = arrivalInfoList.stream()
                            .filter(arrivalInfo -> arrivalInfo.getRouteId().equals(routeInfo.getRouteId()))
                            .findFirst();

                    // 합치기
                    return CombineArrivalRoute.builder()
                            .routeId(routeInfo.getRouteId())
                            .routeName(routeInfo.getRouteName())
                            .routeOrder(routeInfo.getRouteOrder())
                            .direction(routeInfo.getDirection())
                            .arsId(routeInfo.getArsId())
                            .stationId(routeInfo.getStationId())
                            .routeType(matchArrivalInfo.map(ArrivalInfo::getRouteType).orElse(NODATA.name()))
                            .firstArrivalBusCrowding(matchArrivalInfo.map(ArrivalInfo::getFirstArrivalBusCrowding).orElse(NODATA.name()))
                            .firstArrivalBusTime(matchArrivalInfo.map(ArrivalInfo::getFirstArrivalBusTime).orElse(NODATA.getCode3()))
                            .firstArrivalTimeBefore(matchArrivalInfo.map(ArrivalInfo::getFirstArrivalTimeBefore).orElse(NODATA.name()))
                            .secondArrivalBusCrowding(matchArrivalInfo.map(ArrivalInfo::getSecondArrivalBusCrowding).orElse(NODATA.name()))
                            .secondArrivalBusTime(matchArrivalInfo.map(ArrivalInfo::getSecondArrivalBusTime).orElse(NODATA.getCode3()))
                            .secondArrivalTimeBefore(matchArrivalInfo.map(ArrivalInfo::getSecondArrivalTimeBefore).orElse(NODATA.name()))
                            .build();
                }).collect(Collectors.toList());
        return combineArrivalRouteList;
    }
}