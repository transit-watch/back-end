package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transit.transitwatch.entity.BusStopHourlyData;

public interface BusStopHourlyDataRepository extends JpaRepository<BusStopHourlyData, Long> {
}
