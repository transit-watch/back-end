package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transit.transitwatch.entity.BusStopCrowding;

@Repository
public interface BusStopCrowdingRepository extends JpaRepository<BusStopCrowding, Long> {
}