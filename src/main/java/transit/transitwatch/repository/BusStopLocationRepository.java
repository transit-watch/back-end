package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transit.transitwatch.entity.BusStopLocation;

import java.util.Optional;

@Repository
public interface BusStopLocationRepository extends JpaRepository<BusStopLocation, Long> {
    Optional<BusStopLocation> findByArsId(String arsId);
}
