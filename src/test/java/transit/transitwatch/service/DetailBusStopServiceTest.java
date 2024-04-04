package transit.transitwatch.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import transit.transitwatch.dto.arrival.ItemArrival;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.dto.response.ArrivalInfo;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.repository.DetailBusStopRepositoryCustom;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.ItisCdEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class DetailBusStopServiceTest {
    @Autowired
    BusArrivalService busArrivalService;
    @Autowired
    DetailBusStopService detailBusStopService;
    @Autowired
    DetailBusStopRepositoryCustom detailBusStopRepositoryCustom;
    @Autowired
    ApiUtil apiUtil;

    @DisplayName("버스정류장 정보 상세 조회")
    @Test
    void getDetailBusStop() throws Exception {

        List<RouteInfo> routeInfoList = detailBusStopRepositoryCustom.searchDetailBusStopList("02137");
        List<ItemArrival> itemList = new ArrayList<>();
        List<ArrivalInfo> arrivalInfoList = new ArrayList<>();
        for (RouteInfo busStop : routeInfoList) {
            CommonApiDTO<ItemArrival> busArrivalInformationApi;
            try {
                // 각각 버스 노선마다 도착시간 + 혼잡정보 가져오기
                busArrivalInformationApi = busArrivalService.getBusArrivalInformationApi(busStop.getStationId(), busStop.getRouteId(), busStop.getRouteOrder());
                itemList = busArrivalInformationApi.getMsgBody().getItemList();

                if (itemList != null) {
                    List<ArrivalInfo> tempArrivalInfoList = itemList.stream().map(item -> {
                        // itisCd(혼잡여부 코드)랑 일치하는 enum 반환. 없으면 여유 반환
                        ItisCdEnum crowdingEnum1 = (item.getFirstBusDiv() == 4) ? apiUtil.getCrowdingEnum(item.getFirstBusCrowding()) : ItisCdEnum.NODATA;
                        ItisCdEnum crowdingEnum2 = (item.getSecondBusDiv() == 4) ? apiUtil.getCrowdingEnum(item.getSecondBusCrowding()) : ItisCdEnum.NODATA;

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
                    }).collect(Collectors.toList());

                    arrivalInfoList.addAll(tempArrivalInfoList);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
//        DetailBusStopResponse response = DetailBusStopResponse.builder()
//            //    .station(new Station())
//                .combineArrivalRouteList(routeInfoList)
//                            .arrivalList(arrivalInfoList).build();
//        System.out.println("response = " + response);
    }
}