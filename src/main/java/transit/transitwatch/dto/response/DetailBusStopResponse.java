package transit.transitwatch.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@NoArgsConstructor
@ToString
@Getter
public class DetailBusStopResponse {
    private Station station;
    private List<RouteInfo> routeList;
}
