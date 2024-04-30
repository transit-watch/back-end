package transit.transitwatch.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.dto.response.NearByBusStopResponse;
import transit.transitwatch.service.NearByBusStopService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NearByBusStopController.class)
class NearByBusStopControllerTest extends AbstractRestDocsTests {
    @MockBean
    private NearByBusStopService nearByBusStopService;

    @DisplayName("근처 정류장 목록 조회")
    @Test
    void testSearchNearByBusStop() throws Exception {
        double tmX = 126.9784;
        double tmY = 37.5665;
        int radius = 500;

        List<NearByBusStopResponse> fakeResponse = Arrays.asList(
                new NearByBusStopResponse("102900092", "도원삼성래미안아파트단지내", "03737", 126.9553881353, 37.5381983039, 48, "EASYGOING"),
                new NearByBusStopResponse("102900096", "도원삼성래미안아파트101동앞", "03520", 126.9562965631, 37.5390222559, 75, "EASYGOING"),
                new NearByBusStopResponse("102900097", "도원삼성래미안아파트101동앞", "03511", 126.9562320809, 37.5390685906, 77, "EASYGOING")
                );

        given(nearByBusStopService.getNearByBusStopResponses(any())).willReturn(fakeResponse);

        mockMvc.perform(get("/api/v1/bus-stops/near")
                        .param("tmX", String.valueOf(tmX))
                        .param("tmY", String.valueOf(tmY))
                        .param("radius", String.valueOf(radius))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}