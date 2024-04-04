package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusStopInfo;

@Repository
public interface BusStopInfoRepository extends JpaRepository<BusStopInfo, Long> {

    BusStopInfo findByArsId(String arsId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bus_stop_info (station_id, station_name, ars_id, link_id, x_latitude, y_longitude, use_yn, virtual_bus_stop_yn, register_date, edit_date) " +
            "VALUES (:stationId, :stationName, :arsId, :linkId, :xLatitude, :yLongitude, :useYN, :virtualBusStopYN, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) AS new_val " +
            "ON DUPLICATE KEY UPDATE " +
            "station_name = new_val.station_name," +
            "ars_id = new_val.ars_id," +
            "link_id = new_val.link_id," +
            "x_latitude = new_val.x_latitude," +
            "y_longitude = new_val.y_longitude," +
            "use_yn = new_val.use_yn," +
            "virtual_bus_stop_yn = new_val.virtual_bus_stop_yn, " +
            "edit_date = CURRENT_TIMESTAMP", nativeQuery = true)
    void upsertBusStopInfo(String stationId, String stationName, String arsId, String linkId, double xLatitude, double yLongitude, char useYN, char virtualBusStopYN);
}
