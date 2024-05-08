package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusStopInfo;
import transit.transitwatch.repository.projections.SearchKeywordProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusStopInfoRepository extends JpaRepository<BusStopInfo, Long> {

    BusStopInfo findByArsId(String arsId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bus_stop_info (station_id, station_name, ars_id, link_id, y_latitude, x_longitude, use_yn, virtual_bus_stop_yn, register_date, edit_date) " +
            "VALUES (:stationId, :stationName, :arsId, :linkId, :yLatitude, :xLongitude, :useYN, :virtualBusStopYN, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) AS new_val " +
            "ON DUPLICATE KEY UPDATE " +
            "station_name = new_val.station_name," +
            "ars_id = new_val.ars_id," +
            "link_id = new_val.link_id," +
            "y_latitude = new_val.y_latitude," +
            "x_longitude = new_val.x_longitude," +
            "use_yn = new_val.use_yn," +
            "virtual_bus_stop_yn = new_val.virtual_bus_stop_yn, " +
            "edit_date = CURRENT_TIMESTAMP", nativeQuery = true)
    void upsertBusStopInfo(String stationId, String stationName, String arsId, String linkId, double yLatitude, double xLongitude, char useYN, char virtualBusStopYN);

    @Query(value = "SELECT " +
            "    bsi.station_id AS stationId, " +
            "    bsi.station_name AS stationName, " +
            "    bsi.ars_id AS arsId, " +
            "    IF(bsl.y_latitude IS NOT NULL, bsl.y_latitude, bsi.y_latitude) AS yLatitude, " +
            "    IF(bsl.x_longitude IS NOT NULL, bsl.x_longitude, bsi.x_longitude) AS xLongitude, " +
            "    next_bsi.station_name AS nextStationName " +
            "FROM bus_stop_info bsi " +
            "LEFT JOIN bus_stop_location bsl ON bsl.station_id = bsi.station_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        br.station_id AS stationId, " +
            "        MAX(next_br.station_id) AS nextStationId " +
            "    FROM " +
            "        bus_route br " +
            "    LEFT JOIN bus_route next_br ON br.route_order + 1 = next_br.route_order " +
            "        AND br.route_id = next_br.route_id " +
            "    GROUP BY " +
            "        br.station_id " +
            ") next_direction ON bsi.station_id = next_direction.stationId " +
            "LEFT JOIN bus_stop_info next_bsi ON next_direction.nextStationId = next_bsi.station_id",
            nativeQuery = true)
    Optional<List<SearchKeywordProjection>> selectBusStopInfo();

}
