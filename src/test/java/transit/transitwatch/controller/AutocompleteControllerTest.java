package transit.transitwatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.service.AutocompleteService;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutocompleteController.class)
class AutocompleteControllerTest extends AbstractRestDocsTests {
    @MockBean
    AutocompleteService autocompleteService;

    @Test
    void searchBusStopInfo() throws Exception {

        String prefix = "도원";
        Set<String> mockResponse = new HashSet<>();
        mockResponse.add("도원삼성래미안아파트단지내");
        mockResponse.add("도원삼성래미안아파트101동앞");

        given(autocompleteService.getAutocomplete(prefix, "autocomplete:station")).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/bus-stops/autocomplete/{prefix}", prefix)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0]").value("도원삼성래미안아파트101동앞"));
    }

    @Test
    void testSearchBusStopInfo() throws Exception {

        mockMvc.perform(get("/api/v1/bus-stops/autocomplete")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Redis 자동완성 정류장 명 키 값 세팅 성공"));

    }
}