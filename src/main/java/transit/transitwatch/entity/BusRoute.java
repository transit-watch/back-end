package transit.transitwatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import transit.transitwatch.dto.BusRouteDTO;

import java.sql.Timestamp;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "BUS_ROUTE")
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROUTE_ID", length = 9)
    private String routeId;

    @Column(name = "ROUTE_NAME", length = 30)
    private String routeName;

    @Column(name = "ROUTE_ORDER")
    private Integer routeOrder;

    @Column(name = "STATION_ID", length = 9)
    private String stationId;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @Column(name = "X_LATITUDE")
    private Double xLatitude;

    @Column(name = "Y_LONGITUDE")
    private Double yLongitude;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public void setBusRouteDTO(BusRouteDTO busRouteDTO){
        this.routeId = busRouteDTO.getRouteId();
        this.routeName = busRouteDTO.getRouteName();
        this.routeOrder = busRouteDTO.getRouteOrder();
        this.stationId = busRouteDTO.getStationId();
        this.arsId = busRouteDTO.getArsId();
        this.xLatitude = busRouteDTO.getXLatitude();
        this.yLongitude = busRouteDTO.getYLongitude();
    }
}