package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transit.transitwatch.entity.NearByBusStop;

public interface NearByBusStopRepository extends JpaRepository<NearByBusStop, Long> {
}
