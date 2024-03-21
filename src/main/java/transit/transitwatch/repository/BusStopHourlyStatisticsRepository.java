package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transit.transitwatch.entity.BusStopHourlyStatistics;

public interface BusStopHourlyStatisticsRepository extends JpaRepository<BusStopHourlyStatistics, Long> {
}
