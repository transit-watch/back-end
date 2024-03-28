package transit.transitwatch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import transit.transitwatch.entity.BusRoute;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ToString
@Getter
public class BusRouteDTO {
    private String routeId;
    private String routeName;
    private int routeOrder;
    private String stationId;
    private String arsId;
    private double xLatitude;
    private double yLongitude;

    public BusRouteDTO(String routeId, String routeName, int routeOrder, String stationId, String arsId, double xLatitude, double yLongitude) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.stationId = stationId;
        this.arsId = arsId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
    }

    /*
    * Entity로 변환
    * */
    public BusRoute toEntity() {
        return new BusRoute(
                routeId,
                routeName,
                routeOrder,
                stationId,
                arsId,
                xLatitude,
                yLongitude
        );
    }
}
