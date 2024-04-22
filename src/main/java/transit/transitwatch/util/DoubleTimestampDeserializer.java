package transit.transitwatch.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * JSON에서 double 형식의 시간 표현을 Timestamp 객체로 역직렬화하는 데 사용하는 클래스.
 */
public class DoubleTimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        double date = jsonParser.getDoubleValue();
        long time = (long) date;

        return new Timestamp(time);
    }
}