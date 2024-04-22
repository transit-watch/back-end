package transit.transitwatch.repository;

import transit.transitwatch.dto.response.RouteInfo;

import java.util.List;


public interface DetailBusStopRepositoryCustom {

    List<RouteInfo> searchDetailBusStopList(String arsId);
}