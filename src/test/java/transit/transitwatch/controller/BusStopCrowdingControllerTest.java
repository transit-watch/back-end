package transit.transitwatch.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.service.BusStopCrowdingService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusStopCrowdingController.class)
class BusStopCrowdingControllerTest extends AbstractRestDocsTests {

    @MockBean
    private BusStopCrowdingService busStopCrowdingService;

    @DisplayName("버스 정류장 혼잡도 조회")
    @Test
    void testSaveBusStopCrowding() throws Exception {

        mockMvc.perform(post("/api/v1/bus-stops/crowding?pageNo=1&numOfRows=30"))
                .andExpect(status().isOk());

        verify(busStopCrowdingService).saveBusStopCrowdingApi(1, 30);
    }
}