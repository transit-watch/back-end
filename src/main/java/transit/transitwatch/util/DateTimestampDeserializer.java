package transit.transitwatch.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import transit.transitwatch.exception.CustomException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * JSON에서 date 형식의 문자열을 Timestamp 객체로 역직렬화하는 데 사용하는 클래스.
 */
@Slf4j
public class DateTimestampDeserializer extends JsonDeserializer<Timestamp> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    @Override
    public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String date = jsonParser.getText();
        try {
            return new Timestamp(sdf.parse(date).getTime());
        } catch (ParseException e) {
            log.error("date 형식을 Timestamp로 파싱 중 오류가 발생했습니다. : 날짜={} ", date, e);
            throw new CustomException(ErrorCode.PARSING_FAIL, e.getMessage());
        }
    }
}