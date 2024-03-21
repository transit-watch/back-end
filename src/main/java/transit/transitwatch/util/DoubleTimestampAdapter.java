package transit.transitwatch.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;

/*
 * double 형식을 Timestamp로 변환
 * */
public class DoubleTimestampAdapter extends TypeAdapter<Timestamp> {

    @Override
    public void write(JsonWriter jsonWriter, Timestamp timestamp) throws IOException {
        if (timestamp == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(timestamp.getTime());
        }
    }
    @Override
    public Timestamp read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        long time = (long) jsonReader.nextDouble();
        return new Timestamp(time);
    }
}