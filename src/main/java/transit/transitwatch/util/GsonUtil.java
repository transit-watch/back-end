package transit.transitwatch.util;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import transit.transitwatch.dto.BusStopCrowdingDTO;

import java.util.Arrays;
import java.util.List;

@Component
public class GsonUtil {

    /*
    * 버스 정류장 혼잡도 파싱하기
    * */
    public List<BusStopCrowdingDTO> busStopCrowdingParser(String jsonString) {
        Gson gson = new Gson();

        BusStopCrowdingDTO[] busStopCrowdingDataArray = gson.fromJson(jsonString, BusStopCrowdingDTO[].class);
        return Arrays.asList(busStopCrowdingDataArray);
    }

}
