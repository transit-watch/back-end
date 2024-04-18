package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.arrival.ItemArrival;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.response.ArrivalInfo;
import transit.transitwatch.dto.response.CombineArrivalRoute;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.exception.ServiceException;
import transit.transitwatch.repository.DetailBusStopRepositoryCustom;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static transit.transitwatch.util.ErrorCode.API_REQUEST_FAIL;
import static transit.transitwatch.util.ErrorCode.SEARCH_FAIL;
import static transit.transitwatch.util.ItisCdEnum.NODATA;

/**
 * 버스 정류장의 상세 정보 및 도착 정보를 관리하는 서비스 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DetailBusStopService {

    private final DetailBusStopRepositoryCustom detailBusStopRepositoryCustom;
    private final BusArrivalService busArrivalService;
    private final ApiUtil apiUtil;

    /**
     * 지정된 ARS ID를 사용하여 해당 버스 정류장의 상세 정보를 조회한다.
     *
     * @param arsId 버스 정류장의 ARS ID
     * @return 조회된 버스 정류장의 노선 정보 리스트
     * @throws ServiceException 조회 중 오류 발생 시
     */
    public List<RouteInfo> getDetailBusStop(String arsId) {

        try {
            return detailBusStopRepositoryCustom.searchDetailBusStopList(arsId);
        } catch (Exception e) {
            log.error("ARS ID로 버스 정류장 상세 조회 중 오류가 발생했습니다. : ARS ID={}", arsId, e);
            throw new ServiceException(SEARCH_FAIL);
        }
    }

    /**
     * 버스 도착 정보 항목을 바탕으로 도착 정보 객체를 생성한다.
     *
     * @param item 도착 정보 항목
     * @return 생성된 ArrivalInfo 객체
     * @throws ServiceException 도착 정보 생성 중 오류 발생 시
     */
    public ArrivalInfo getArrivalInfo(ItemArrival item) {
        try {
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
        } catch (Exception e) {
            log.error("도착 정보 생성 중 오류가 발생했습니다. : {}", item, e);
            throw new ServiceException(API_REQUEST_FAIL);
        }
    }

    /**
     * 여러 노선의 버스 정류장에 대한 도착 정보를 조회한다.
     *
     * @param routeInfoList 노선 정보 리스트
     * @return 각 노선에 대한 도착 정보 리스트
     * @throws ServiceException 도착 정보 API 호출 중 오류 발생 시
     */
    public List<ArrivalInfo> getArrivalInfo(List<RouteInfo> routeInfoList) {
        List<ArrivalInfo> arrivalInfoList = new ArrayList<>();
        for (RouteInfo busStop : routeInfoList) {
            try {
                CommonApiDTO<ItemArrival> busArrivalInformationApi = busArrivalService.getBusArrivalInformationApi(busStop.getStationId(), busStop.getRouteId(), busStop.getRouteOrder());
                List<ItemArrival> itemList = Optional.ofNullable(busArrivalInformationApi.getMsgBody().getItemList()).orElse(Collections.emptyList());
                List<ArrivalInfo> tempArrivalInfoList = itemList.stream()
                        .map(this::getArrivalInfo)
                        .collect(Collectors.toList());

                arrivalInfoList.addAll(tempArrivalInfoList);
            } catch (Exception e) {
                log.error("도착 정보 API 호출 중 오류가 발생했습니다. 노선 정보: {}", busStop, e);
                throw new ServiceException(API_REQUEST_FAIL);
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

        return routeInfoList.stream().map( // 노선 정보 가져오기
                routeInfo -> {
                    // routeId가 일치하는 ArrivalInfo 찾기, 없으면 null
                    Optional<ArrivalInfo> matchArrivalInfo = arrivalInfoList.stream()
                            .filter(arrivalInfo -> arrivalInfo.getRouteId().equals(routeInfo.getRouteId()))
                            .findFirst();

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
    }
}