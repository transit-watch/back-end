package transit.transitwatch.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiUtil apiUtil;

    @Test
    void getApi() throws Exception {
        String url = "https://example.com/api";
        String expectedResponse = "{\"key\":\"value\"}";

        when(restTemplate.getForObject(url, String.class)).thenReturn(expectedResponse);
        String actualResponse = apiUtil.getApi(url);

        verify(restTemplate, times(1)).getForObject(url, String.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void removeBOM() {
        String test = "test\ufeff";
        String bom = apiUtil.removeBOM(test);
        assertThat(bom).isEqualTo("test");
//        assertThat(bom).isNotEqualTo("test");
    }
}