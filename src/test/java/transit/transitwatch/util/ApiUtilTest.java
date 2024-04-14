package transit.transitwatch.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        URI convertUri = new URI(url);
        String expectedResponse = "{\"key\":\"value\"}";

        when(restTemplate.getForObject(convertUri, String.class)).thenReturn(expectedResponse);
        String actualResponse = apiUtil.getApiUri(convertUri);

        verify(restTemplate, times(1)).getForObject(convertUri, String.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void removeBOM() {
        String test = "test\ufeff";
        String bom = apiUtil.removeBOM(test);
        assertThat(bom).isEqualTo("test");
//        assertThat(bom).isNotEqualTo("test");
    }

    @DisplayName("정규식 테스트")
    @Test
    void 정규식테스트() {
        String input = "4분15초후[2번째 전]";
        String regex = "\\[(.*?)\\]";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        String group = null;

        if (matcher.find()) {
            group = matcher.group(1);
            System.out.println(group);
        }
        assertThat(group).isEqualTo("2번째 전");
    }

    @DisplayName("enum 테스트")
    @Test
    void enum테스트() {
        String code = "4";
        for (ItisCdEnum itisCdEnum : ItisCdEnum.values()) {
            if (code.equals(itisCdEnum.getCode()) || code.equals(itisCdEnum.getCode2())) {
                System.out.println("itisCdEnum = " + itisCdEnum);
                assertThat(itisCdEnum).isEqualTo(ItisCdEnum.NORMAL);
            }
        }
    }
}