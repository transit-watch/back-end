package transit.transitwatch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class SearchKeywordDTO {

    private String stationId;
    private String stationName;
    private String arsId;
    private String yLatitude;
    private String xLongitude;
    @JsonProperty("direction")
    private String nextStationName;

    @Builder
    public SearchKeywordDTO(String stationId, String stationName, String arsId, String yLatitude, String xLongitude, String nextStationName) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.yLatitude = yLatitude;
        this.xLongitude = xLongitude;
        this.nextStationName = nextStationName;
    }
    public double distance(String yLatitude, String xLongitude) {
        // 간단한 유클리디언 거리 계산 예제
        return Math.sqrt(Math.pow(Double.parseDouble(this.yLatitude) - Double.parseDouble(yLatitude), 2)
                + Math.pow(Double.parseDouble(this.xLongitude) - Double.parseDouble(xLongitude), 2));
    }
}
