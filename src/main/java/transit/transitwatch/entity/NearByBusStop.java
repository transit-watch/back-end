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
@Table(name = "NEAR_BY_BUS_STOP")
public class NearByBusStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATION_ID", length = 9)
    private String stationId;

    @Column(name = "X_LATITUDE")
    private double xLatitude;

    @Column(name = "Y_LONGITUDE")
    private double yLongitude;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @Column(name = "DISTANCE")
    private int distance;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public NearByBusStop(String stationId, double xLatitude, double yLongitude, String arsId, int distance) {
        this.stationId = stationId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
        this.arsId = arsId;
        this.distance = distance;
    }
}