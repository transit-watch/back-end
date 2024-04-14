package transit.transitwatch.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class Station {
    private String stationName;
    private String nextStationName;
    private String arsId;
    private String crowding;
    private double xLatitude;
    private double yLongitude;
}
