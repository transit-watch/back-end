package transit.transitwatch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.dto.common.CommonApiDTO;

import java.util.Arrays;
import java.util.List;

@Component
public class ApiJsonParser {

    /*
    * 버스 정류장 혼잡도 파싱하기
    * */
    public List<BusStopCrowdingDTO> busStopCrowdingParser(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();

        BusStopCrowdingDTO[] busStopCrowdingDataArray = null;
        try {
            busStopCrowdingDataArray = objectMapper.readValue(jsonString, BusStopCrowdingDTO[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Arrays.asList(busStopCrowdingDataArray);
    }

    /*
     * 버스 정류장 근처 좌표기반 조회 파싱하기
     * */
    public CommonApiDTO nearByBusStopParser(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();

        CommonApiDTO CommonApiDTO = null;
        try {
            CommonApiDTO = objectMapper.readValue(jsonString, CommonApiDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return CommonApiDTO;
    }

}
