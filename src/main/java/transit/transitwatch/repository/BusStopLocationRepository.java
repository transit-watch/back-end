package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transit.transitwatch.entity.BusStopLocation;

@Repository
public interface BusStopLocationRepository extends JpaRepository<BusStopLocation, Long> {
    BusStopLocation findByStationId(String stationId);
}
