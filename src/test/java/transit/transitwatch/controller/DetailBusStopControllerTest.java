package transit.transitwatch.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.dto.BusStopLocationDTO;
import transit.transitwatch.dto.response.ArrivalInfo;
import transit.transitwatch.dto.response.CombineArrivalRoute;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.dto.response.Station;
import transit.transitwatch.service.BusStopCrowdingService;
import transit.transitwatch.service.BusStopLocationService;
import transit.transitwatch.service.DetailBusStopService;
import transit.transitwatch.util.ItisCdEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetailBusStopController.class)
class DetailBusStopControllerTest extends AbstractRestDocsTests {

    @MockBean
    private BusStopLocationService busStopLocationService;

    @MockBean
    private DetailBusStopService detailBusStopService;

    @MockBean
    private BusStopCrowdingService busStopCrowdingService;

    @MockBean
    private ItisCdEnum itisCdEnum;

    @DisplayName("버스 정류장 상세조회")
    @Test
    void testSearchDetailBusStop() throws Exception {

        String arsId = "02286";

        BusStopLocationDTO locationDTO = new BusStopLocationDTO("101000290", "시청앞.덕수궁", "02286", 37.56621228, 126.9768356, "중앙");
        when(busStopLocationService.selectBusStopLocation(arsId)).thenReturn(Optional.of(locationDTO));

        RouteInfo routeInfo = new RouteInfo("100100185", "7022", 34, "삼성본관앞", "101000290", "02286");
        when(detailBusStopService.getDetailBusStop(arsId)).thenReturn(Arrays.asList(routeInfo));

        ArrivalInfo arrivalInfo = new ArrivalInfo("100100185", "1", 30, 360, "삼성본관앞", itisCdEnum.name(), itisCdEnum.name(), "NODATA", "NODATA");
        when(detailBusStopService.getArrivalInfo(Arrays.asList(routeInfo))).thenReturn(Arrays.asList(arrivalInfo));

        List<CombineArrivalRoute> combineArrivalRouteList = Arrays.asList(
                new CombineArrivalRoute("100100185", "7022", 34, "삼성본관앞", "101000290", "02286", "1", 30, 360, "삼성본관앞", itisCdEnum.name(), itisCdEnum.name(), "NODATA", "NODATA")
        );
        when(detailBusStopService.getCombineRoute(Arrays.asList(routeInfo), Arrays.asList(arrivalInfo))).thenReturn(combineArrivalRouteList);

        when(busStopCrowdingService.selectBusStopCrowding(arsId)).thenReturn(ItisCdEnum.EASYGOING);

        Station station = Station.builder()
                .arsId(arsId)
                .yLatitude(locationDTO.getYLatitude())
                .xLongitude(locationDTO.getXLongitude())
                .stationName(locationDTO.getStationName())
                .nextStationName(routeInfo.getDirection())
                .crowding(itisCdEnum.name())
                .build();

        mockMvc.perform(get("/api/v1/bus-stops/detail/{arsId}", arsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.station.stationName").value("시청앞.덕수궁"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.station.nextStationName").value("삼성본관앞"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.station.crowding").value("EASYGOING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.busList[0].stationId").value("101000290"));

    }
}