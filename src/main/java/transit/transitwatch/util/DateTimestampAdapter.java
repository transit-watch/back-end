package transit.transitwatch.util;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
* date 형식을 Timestamp로 변환
* */
public class DateTimestampAdapter extends TypeAdapter<Timestamp> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    @Override
    public void write(JsonWriter jsonWriter, Timestamp timestamp) throws IOException {
        if (timestamp == null) {
            jsonWriter.nullValue();
        } else {
           String date = sdf.format(new Date(timestamp.getTime()));
            jsonWriter.value(date);

        }
    }
    @Override
    public Timestamp read(JsonReader jsonReader) throws IOException {
        try {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new Timestamp(sdf.parse(jsonReader.nextString()).getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}