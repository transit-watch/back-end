package transit.transitwatch.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.service.BusStopCrowdingService;
import transit.transitwatch.util.ApiUtil;
import transit.transitwatch.util.GsonUtil;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@WebMvcTest(BusStopCrowdingController.class)
class BusStopCrowdingControllerTest extends AbstractRestDocsTests {

    @MockBean
    private BusStopCrowdingService busStopCrowdingService;

    @MockBean
    private GsonUtil gsonUtil;

    @MockBean
    private ApiUtil apiUtil;


    @Disabled
    @Test
    void 버스정류장_혼잡도_조회() throws Exception {

        mockMvc.perform(get("/api/busStopCrowding"))
                .andExpect(status().isOk());
       // BusStopCrowdingService

    }
}