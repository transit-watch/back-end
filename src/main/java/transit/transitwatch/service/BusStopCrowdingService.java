package transit.transitwatch.service;


import transit.transitwatch.dto.BusStopCrowdingDTO;
import transit.transitwatch.entity.BusStopCrowding;

import java.util.List;

public interface BusStopCrowdingService {
    void saveBusStopCrowdingApi() throws Exception;
    List<BusStopCrowding> selectBusStopCrowding(BusStopCrowdingDTO busStopCrowdingDTO) throws Exception;

}
