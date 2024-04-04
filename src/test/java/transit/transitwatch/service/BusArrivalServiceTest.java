package transit.transitwatch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import transit.transitwatch.dto.arrival.ItemArrival;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.util.ApiJsonParser;
import transit.transitwatch.util.ApiUtil;

import java.net.URI;

@SpringBootTest
class BusArrivalServiceTest {

    @Autowired
    private ApiJsonParser apiJsonParser;
    @Autowired
    private ApiUtil apiUtil;
    @Autowired
    private BusArrivalService busArrivalService;

    @DisplayName("상세 정류장 목록 조회")
    @Test
    void getBusArrivalInformationApi() {
        URI url = busArrivalService.getApiUrl("101000039", "100100002", 12);
        String apiResult;

        // api 받아오기
        try {
            apiResult = apiUtil.getApiUri(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // api 결과 dto에 넣기
        CommonApiDTO<ItemArrival> commonApiDTO = apiJsonParser.busGoKrParser(apiResult, new TypeReference<>() {});

        System.out.println(commonApiDTO);
    }
}