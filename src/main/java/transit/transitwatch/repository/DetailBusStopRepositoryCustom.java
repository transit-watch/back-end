package transit.transitwatch.repository;

import org.springframework.stereotype.Repository;
import transit.transitwatch.dto.response.RouteInfo;

import java.util.List;


public interface DetailBusStopRepositoryCustom {

    List<RouteInfo> searchDetailBusStopList(String arsId);
}