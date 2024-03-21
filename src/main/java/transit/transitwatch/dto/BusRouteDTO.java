package transit.transitwatch.dto;

import lombok.Data;

@Data
public class BusRouteDTO {
    private String routeId;
    private String routeName;
    private Integer routeOrder;
    private String stationId;
    private String arsId;
    private Double xLatitude;
    private Double yLongitude;

    public BusRouteDTO(String routeId, String routeName, Integer routeOrder, String stationId, String arsId, Double xLatitude, Double yLongitude) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.stationId = stationId;
        this.arsId = arsId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
    }
}
