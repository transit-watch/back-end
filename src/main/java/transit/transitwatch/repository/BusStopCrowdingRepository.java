package transit.transitwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusStopCrowding;
import transit.transitwatch.repository.projections.BusStopCrowdingProjection;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusStopCrowdingRepository extends JpaRepository<BusStopCrowding, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bus_stop_crowding (itis_cd, send_utc_time, y_longitude, x_latitude, link_id, ars_id" +
            ", send_packet_year, send_packet_month, send_packet_day, send_packet_time, send_packet_milisecond, record_date, register_date, edit_date) " +
            "VALUES (:itisCd, :sendUtcTime, :yLongitude, :xLatitude, :linkId, :arsId, :sendPacketYear," +
            " :sendPacketMonth, :sendPacketDay, :sendPacketTime, :sendPacketMilisecond" +
            ", :recordDate, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) AS new_val " +
            "ON DUPLICATE KEY UPDATE " +
            "send_utc_time = new_val.send_utc_time, " +
            "y_longitude = new_val.y_longitude, " +
            "x_latitude = new_val.x_latitude, " +
            "link_id = new_val.link_id, " +
            "send_packet_year = new_val.send_packet_year, " +
            "send_packet_month = new_val.send_packet_month, " +
            "send_packet_day = new_val.send_packet_day, " +
            "send_packet_time = new_val.send_packet_time, " +
            "send_packet_milisecond = new_val.send_packet_milisecond, " +
            "record_date = new_val.record_date, " +
            "edit_date = CURRENT_TIMESTAMP", nativeQuery = true)
    void upsertBusStopCrowding(String itisCd, Timestamp sendUtcTime, double yLongitude, double xLatitude, String linkId, String arsId, String sendPacketYear, String sendPacketMonth, String sendPacketDay, String sendPacketTime, String sendPacketMilisecond, Timestamp recordDate);

    Optional<BusStopCrowdingProjection> findByArsId(String arsId);

    List<BusStopCrowdingProjection> findByArsIdIn(List<String> arsIdList);
}