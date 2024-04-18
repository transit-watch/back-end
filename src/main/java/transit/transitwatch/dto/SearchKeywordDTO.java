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
    private String xLatitude;
    private String yLongitude;
    @JsonProperty(value = "direction")
    private String nextStationName;

    @Builder
    public SearchKeywordDTO(String stationId, String stationName, String arsId, String xLatitude, String yLongitude, String nextStationName) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
        this.nextStationName = nextStationName;
    }
    public double distance(String xLatitude, String yLongitude) {
        // 간단한 유클리디언 거리 계산 예제
        return Math.sqrt(Math.pow(Double.parseDouble(this.xLatitude) - Double.parseDouble(xLatitude), 2)
                + Math.pow(Double.parseDouble(this.yLongitude) - Double.parseDouble(yLongitude), 2));
    }
}
