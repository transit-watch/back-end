package transit.transitwatch.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class BusStopLocationDTO {
    private String stationId;
    private String stationName;
    private String arsId;

    @JsonProperty("ylatitude")
    private double yLatitude;

    @JsonProperty("xlongitude")
    private double xLongitude;

    private String busStopType;

    @Builder
    public BusStopLocationDTO(String stationId, String stationName, String arsId, double yLatitude, double xLongitude, String busStopType) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.yLatitude = yLatitude;
        this.xLongitude = xLongitude;
        this.busStopType = busStopType;
    }
}