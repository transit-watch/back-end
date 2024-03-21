package transit.transitwatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.service.BusStopInfoService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusStopInfoController.class)
class BusStopInfoControllerTest extends AbstractRestDocsTests {

    @MockBean
    private BusStopInfoService busStopInfoService;

    @Test
    void 버스정류장_파일저장() throws Exception {
        mockMvc.perform(get("/api/save/busStopInfo"))
                .andExpect(status().isOk());

        verify(busStopInfoService).saveBusStopInfoFile("bus_stop_info.csv");
    }
}