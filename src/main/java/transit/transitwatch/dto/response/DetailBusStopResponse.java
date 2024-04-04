package transit.transitwatch.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@NoArgsConstructor
@ToString
@Getter
public class DetailBusStopResponse {
    private Station station;
    @JsonProperty(value = "busList")
    private List<CombineArrivalRoute> combineArrivalRouteList;

    @Builder
    public DetailBusStopResponse(Station station, List<CombineArrivalRoute> combineArrivalRouteList) {
        this.station = station;
        this.combineArrivalRouteList = combineArrivalRouteList;
    }
}
