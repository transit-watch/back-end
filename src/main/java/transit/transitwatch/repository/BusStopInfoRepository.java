package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transit.transitwatch.entity.BusStopInfo;

@Repository
public interface BusStopInfoRepository  extends JpaRepository<BusStopInfo, Long> {

}
