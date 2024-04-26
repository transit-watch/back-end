package transit.transitwatch.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import transit.transitwatch.config.AbstractRestDocsTests;
import transit.transitwatch.dto.SearchKeywordDTO;
import transit.transitwatch.service.AutocompleteService;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutocompleteController.class)
class AutocompleteControllerTest extends AbstractRestDocsTests {
    @MockBean
    AutocompleteService autocompleteService;

    @DisplayName("자동완성 검색어 조회")
    @Test
    void testSearchAutocomplete() throws Exception {
        String keyword = "시청";
        String x = "126.95584930";
        String y = "37.53843986";

        List<SearchKeywordDTO> result = Arrays.asList(
                new SearchKeywordDTO("101000033", "시청앞", "02128", "37.56219717", "126.9764437", "프레스센터"),
                new SearchKeywordDTO("101000038", "시청.서소문청사", "02135", "37.56352484", "126.9749539", "충정로역2호선"),
                new SearchKeywordDTO("101000035", "시청앞", "02132", "37.56276421", "126.9761487", "숭례문.한국일보"),
                new SearchKeywordDTO("101000279", "시청역더플라자호텔", "02699", "37.5641562", "126.9779059", "소공동웨스틴조선호텔"),
                new SearchKeywordDTO("101000227", "시청덕수궁", "02662", "37.5654923", "126.9769657", "프레이저플레이스남대문"),
                new SearchKeywordDTO("101900011", "시청역", "02503", "37.56567062", "126.9770029", "삼성본관앞"),
                new SearchKeywordDTO("101000290", "시청앞.덕수궁", "02286", "37.56621228", "126.9768356", "시청.서소문청사"),
                new SearchKeywordDTO("101000286", "시청.서울신문사", "02706", "37.56829932", "126.9773647", "광화문")
        );

        Set<String> auto = new HashSet<>();
        auto.add("시청앞");
        auto.add("시청.서소문청사");
        auto.add("시청앞");
        auto.add("시청역더플라자호텔");
        auto.add("시청덕수궁");
        auto.add("시청역");
        auto.add("시청앞.덕수궁");
        auto.add("시청.서울신문사");

        given(autocompleteService.autocomplete(keyword)).willReturn(auto);
        given(autocompleteService.searchAndSortHash(auto, y, x)).willReturn(result);

        mockMvc.perform(get("/api/v1/bus-stops/autocomplete?keyword={keyword}&xLongitude={xLongitude}&yLatitude={yLatitude}"
                        , keyword, x, y)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].stationName").value("시청앞"));
    }

    @DisplayName("자동완성 검색어 저장")
    @Test
    void testSaveAutocomplete() throws Exception {

        mockMvc.perform(post("/api/v1/bus-stops/autocomplete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Redis 자동완성 정류장 명 키 값 세팅 성공"));

    }
}