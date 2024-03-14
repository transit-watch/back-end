package transit.transitwatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import transit.transitwatch.config.AbstractRestDocsTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestDocsTestController.class)
class RestDocsTestControllerTest extends AbstractRestDocsTests {

    @Test
    void 테스트() throws Exception {
        mockMvc.perform(get("/rest")).andExpect(status().isOk());
    }
}