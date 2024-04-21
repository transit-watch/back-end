package transit.transitwatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "BUS_ROUTE", uniqueConstraints = {
        @UniqueConstraint(name = "unique_route_station_ars", columnNames = {"ROUTE_ID", "STATION_ID", "ARS_ID"})
})
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
    private int routeOrder;

    @Column(name = "STATION_ID", length = 9)
    private String stationId;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @Column(name = "Y_LATITUDE")
    private double yLatitude;

    @Column(name = "X_LONGITUDE")
    private double xLongitude;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_ID", insertable = false, updatable = false)
    private BusStopInfo busStopInfo;

    public BusRoute(String routeId, String routeName, int routeOrder, String stationId, String arsId, double yLatitude, double xLongitude) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.stationId = stationId;
        this.arsId = arsId;
        this.yLatitude = yLatitude;
        this.xLongitude = xLongitude;
    }
}