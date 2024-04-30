package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.BusStopLocationDTO;
import transit.transitwatch.entity.BusStopLocation;
import transit.transitwatch.exception.ServiceException;
import transit.transitwatch.repository.BusStopLocationRepository;

import java.util.Optional;

import static transit.transitwatch.util.ErrorCode.SEARCH_FAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class BusStopLocationService {
    private final BusStopLocationRepository busStopLocationRepository;

    /**
     * 버스 정류장 위치 정보를 조회한다. 조회 결과는 'busStopLocation' 캐시에 저장되고 데이터가 없을 경우 빈 Optional을 반환한다.
     *
     * @param arsId 버스 정류장을 식별하는 ARS ID
     * @return ARS ID에 해당하는 버스 정류장의 위치 정보를 포함하는 Optional 객체. 정보가 없으면 빈 Optional 반환.
     * @throws ServiceException 버스 정류장 위치 정보 조회 중 오류 발생 시
     * @Cacheable 메서드의 결과를 "busStopLocation" 캐시에 저장하고, 동일한 arsId로 요청이 있을 때 캐시된 데이터를 반환한다.
     */
    @Cacheable(cacheNames = "busStopLocation", key = "#arsId", unless = "#result == null")
    public Optional<BusStopLocationDTO> selectBusStopLocation(String arsId) {
        try {
            Optional<BusStopLocation> byArsId = busStopLocationRepository.findByArsId(arsId);

            return byArsId.map(busStopLocation -> BusStopLocationDTO.builder()
                    .stationId(busStopLocation.getStationId())
                    .stationName(busStopLocation.getStationName())
                    .arsId(busStopLocation.getArsId())
                    .xLongitude(busStopLocation.getXLongitude())
                    .yLatitude(busStopLocation.getYLatitude())
                    .busStopType(busStopLocation.getBusStopType())
                    .build()).or(() -> Optional.of(new BusStopLocationDTO()));
        } catch (Exception e) {
            log.error("ARS ID로 버스 정류장 위치 정보 조회 실패: {}", arsId, e);
            throw new ServiceException("ARS ID로 버스 정류장 위치 정보 조회 실패: " + arsId, SEARCH_FAIL);
        }
    }
}
