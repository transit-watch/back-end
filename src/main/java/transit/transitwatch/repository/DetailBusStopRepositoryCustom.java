package transit.transitwatch.repository;

import transit.transitwatch.dto.response.RouteInfo;

import java.util.List;
import java.util.Optional;


public interface DetailBusStopRepositoryCustom {

    Optional<List<RouteInfo>> searchDetailBusStopList(String arsId);
}