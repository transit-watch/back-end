package transit.transitwatch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.dto.common.CommonApiDTO;
import transit.transitwatch.exception.CustomException;

import java.util.Arrays;
import java.util.List;

import static transit.transitwatch.util.ErrorCode.*;

/**
 * JSON 데이터를 파싱하여 DTO 객체로 변환하는 유틸리티 클래스.
 */
@Component
public class ApiJsonParser {

    /**
     * JSON 문자열을 파싱하여 버스 정류장 혼잡도 데이터 리스트로 변환한다.
     *
     * @param jsonString 버스 정류장 혼잡도 데이터를 포함하는 JSON 문자열
     * @return 파싱된 버스 정류장 혼잡도 데이터의 리스트
     * @throws CustomException JSON 파싱에 실패했을 경우 발생
     */
    public List<BusStopCrowdingDTO> busStopCrowdingParser(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Arrays.asList(objectMapper.readValue(jsonString, BusStopCrowdingDTO[].class));
        } catch (JsonProcessingException e) {
            throw new CustomException(PARSING_FAIL);
        }
    }

    /**
     * JSON 문자열을 파싱하여 일반적인 API 응답 형태의 DTO로 변환한다.
     * 이 메서드는 제네릭 타입을 사용하여 다양한 종류의 데이터를 처리할 수 있다.
     *
     * @param jsonString API 응답을 포함하는 JSON 문자열
     * @param typeReference 변환될 DTO의 타입 정보
     * @return 파싱된 데이터를 포함하는 CommonApiDTO 객체
     * @throws CustomException JSON 파싱에 실패했을 경우 발생
     */
    public <T> CommonApiDTO<T> busGoKrParser(String jsonString, TypeReference<CommonApiDTO<T>> typeReference) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new CustomException(PARSING_FAIL);
        }
    }
}
