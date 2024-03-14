package transit.transitwatch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transit.transitwatch.entity.BusRoute;

import java.util.Optional;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {
    Optional<BusRoute> findByStationId(String stationId);


}
