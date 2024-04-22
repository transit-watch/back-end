package transit.transitwatch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import transit.transitwatch.entity.BusRoute;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bus_route (route_id, route_name, route_order, station_id, ars_id, y_latitude, x_longitude, register_date, edit_date) " +
            "VALUES (:routeId, :routeName, :routeOrder, :stationId, :arsId, :yLatitude, :xLongitude, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) " +
            "AS new_val " +
            "ON DUPLICATE KEY UPDATE " +
            "route_name = new_val.route_name, " +
            "route_order = new_val.route_order, " +
            "station_id = new_val.station_id, " +
            "ars_id = new_val.ars_id, " +
            "y_latitude = new_val.y_latitude, " +
            "x_longitude = new_val.x_longitude, " +
            "edit_date = CURRENT_TIMESTAMP", nativeQuery = true)
    void upsertBusRoute(String routeId, String routeName, int routeOrder, String stationId, String arsId, double yLatitude, double xLongitude);

}
