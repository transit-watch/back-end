package transit.transitwatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.service.BusRouteService;
import transit.transitwatch.util.ApiUtil;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusRouteController.class)
class BusRouteControllerTest extends AbstractRestDocsTests {

    @MockBean
    private ApiUtil apiUtil;

    @MockBean
    private BusRouteService busRouteService;

    @Test
    void 버스경로_파일다운로드() throws Exception {
        mockMvc.perform(get("/api/download/busRoute"))
                .andExpect(status().isOk());
        verify(busRouteService).saveBusRouteFile("bus_route.csv");
    }
}