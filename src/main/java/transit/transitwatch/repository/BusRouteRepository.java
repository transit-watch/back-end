package transit.transitwatch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusRoute;

import java.util.Optional;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {

    Optional<BusRoute> findByStationId(String stationId);

    @Transactional
    @Modifying
    @Query(value = "truncate table bus_route_repository", nativeQuery = true)
    void truncateTable();
}
