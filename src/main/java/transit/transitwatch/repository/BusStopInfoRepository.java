package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusStopInfo;

@Repository
public interface BusStopInfoRepository  extends JpaRepository<BusStopInfo, Long> {

    @Transactional
    @Modifying
    @Query(value = "truncate table bus_stop_info", nativeQuery = true)
    void truncateTable();
}
