package transit.transitwatch.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
* 좌표 기반 근처 정류장 목록 조회 - response용 Dto
* */
@NoArgsConstructor
@ToString
@Getter
public class NearByBusStopResponse {

    private String stationId;
    private String stationName;
    private String arsId;
    private double yLatitude;
    private double xLongitude;
    private int distance;
    private String crowding;

    @Builder
    public NearByBusStopResponse(String stationId, String stationName, String arsId, double yLatitude, double xLongitude, int distance, String crowding) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.yLatitude = yLatitude;
        this.xLongitude = xLongitude;
        this.distance = distance;
        this.crowding = crowding;
    }
}
