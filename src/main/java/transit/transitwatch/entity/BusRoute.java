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
    private int routeOrder;

    @Column(name = "STATION_ID", length = 9)
    private String stationId;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @Column(name = "X_LATITUDE")
    private double xLatitude;

    @Column(name = "Y_LONGITUDE")
    private double yLongitude;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public BusRoute(String routeId, String routeName, int routeOrder, String stationId, String arsId, double xLatitude, double yLongitude) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.stationId = stationId;
        this.arsId = arsId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
    }
}