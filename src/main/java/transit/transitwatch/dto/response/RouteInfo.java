package transit.transitwatch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class RouteInfo {
    @JsonProperty(value = "busId")
    private String routeId;
    @JsonProperty(value = "busName")
    private String routeName;
    @JsonProperty(value = "busOrder")
    private int routeOrder;
    private String direction;                   // 방면
    private String stationId;                   // 정류소 고유 ID
    private String arsId;

    // 테스트 코드용 생성자
    public RouteInfo(String routeId, String routeName, int routeOrder, String direction, String stationId, String arsId) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.direction = direction;
        this.stationId = stationId;
        this.arsId = arsId;
    }
}
