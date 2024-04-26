package transit.transitwatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.service.BusRouteService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusRouteController.class)
class BusRouteControllerTest extends AbstractRestDocsTests {

    @MockBean
    private BusRouteService busRouteService;

    @Test
    void testSaveBusRoute() throws Exception {
        mockMvc.perform(post("/api/v1/bus-stops/route"))
                .andExpect(status().isOk());
        verify(busRouteService).saveBusRouteFile("bus_route.csv");
    }
}