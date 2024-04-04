package transit.transitwatch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.dto.common.CommonApiDTO;

import java.util.Arrays;
import java.util.List;

@Component
public class ApiJsonParser {

    /**
     * 버스 정류장 혼잡도 정보를 JSON 문자열에서 파싱하여 DTO 리스트로 변환한다.
     *
     * <p>이 메서드는 JSON 형식의 문자열을 받아 {@link BusStopCrowdingDTO} 객체 배열로 변환하고,
     * 해당 배열을 리스트로 변환하여 반환합니다. 변환 과정에서 발생할 수 있는
     * {@link JsonProcessingException}을 처리한다.</p>
     * @param jsonString 버스 정류장 혼잡도 정보를 포함한 JSON 문자열
     * @return 버스 정류장 혼잡도 정보가 담긴 {@link BusStopCrowdingDTO} 객체 리스트
     * @throws RuntimeException JSON 파싱 중 발생한 예외 발생 시.
     */
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

    /**
     * ws.bus.go.kr API의 응답을 JSON 문자열에서 파싱하여 특정 타입의 데이터를 포함하는
     * {@link CommonApiDTO} 객체로 변환한다.
     * <p>이 메서드는 JSON 문자열을 받아 제네릭 타입의 {@link CommonApiDTO} 객체로 변환한다.
     * 변환 과정에서 발생할 수 있는 {@link JsonProcessingException}을 처리한다. 메서드는
     * 다양한 타입의 API 응답을 처리하기 위해 {@link TypeReference}를 사용한다.</p>
     * @param jsonString 파싱할 JSON 문자열
     * @param typeReference 반환할 {@link CommonApiDTO}의 타입 정보
     * @return 파싱된 데이터를 포함하는 {@link CommonApiDTO} 객체
     * @throws RuntimeException JSON 파싱 중 발생한 예외 발생 시.
     */
    public <T> CommonApiDTO<T> busGoKrParser(String jsonString, TypeReference<CommonApiDTO<T>> typeReference) {

        ObjectMapper objectMapper = new ObjectMapper();
        CommonApiDTO<T> commonApiDTO = null;
        try {
            commonApiDTO = objectMapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return commonApiDTO;
    }
}
