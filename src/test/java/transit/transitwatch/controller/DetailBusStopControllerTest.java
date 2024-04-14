package transit.transitwatch.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.dto.response.RouteInfo;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.service.BusStopCrowdingService;
import transit.transitwatch.service.BusStopInfoService;
import transit.transitwatch.service.DetailBusStopService;
import transit.transitwatch.util.ItisCdEnum;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static transit.transitwatch.util.ItisCdEnum.NODATA;

@WebMvcTest(DetailBusStopController.class)
class DetailBusStopControllerTest extends AbstractRestDocsTests {

    @MockBean
    DetailBusStopService detailBusStopService;
    @MockBean
    BusStopInfoService busStopInfoService;
    @MockBean
    BusStopCrowdingService busStopCrowdingService;
    private ItisCdEnum itisCdEnum;

    @DisplayName("버스 정류장 상세조회")
    @Test
    void detailBusStop() throws Exception {
        String arsId = "02137";
        String stationId = "101000039";
        String stationName = "KT광화문지사";
        String linkId = "123456789";
        double xLatitude = 37.5665;
        double yLongitude = 126.9784;
        char useYN = 'Y';
        char virtualBusStopYN = 'N';
        itisCdEnum = NODATA;

        BusStopInfo mockBusStopInfo = new BusStopInfo(stationId, stationName, arsId, linkId, xLatitude, yLongitude, useYN, virtualBusStopYN);
        List<RouteInfo> routeInfo = Arrays.asList(
                new RouteInfo("101000004", "7475", 1, "KT광화문지사", "101000039", "02137"),
                new RouteInfo("100100002", "03", 22, "KT광화문지사", "101000039", "02137"),
                new RouteInfo("106000002", "04", 7, "KT광화문지사", "101000039", "02137"),
                new RouteInfo("100100001", "01A", 15, "KT광화문지사", "101000039", "02137"),
                new RouteInfo("100100077", "501", 26, "종로1가", "101000039", "02137"),
                new RouteInfo("123000011", "708", 32, "KT광화문지사", "101000039", "02137")
        );

        when(busStopInfoService.selectBusStopArsId(anyString())).thenReturn(mockBusStopInfo);
        when(detailBusStopService.getDetailBusStop(arsId)).thenReturn(routeInfo);
        when(busStopCrowdingService.selectBusStopCrowding(arsId)).thenReturn(itisCdEnum);

        mockMvc.perform(get("/api/v1/bus-stops/detail/{arsId}", arsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}