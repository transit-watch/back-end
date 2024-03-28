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
@Table(name = "BUS_STOP_HOURLY_STATISTICS")
public class BusStopHourlyStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE_YM", length = 6)
    private String dateYm;

    @Column(name = "ROUTE_ID", length = 9)
    private String routeId;

    @Column(name = "STATION_ID", length = 9)
    private String stationId;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public BusStopHourlyStatistics(String dateYm, String routeId, String stationId, String arsId) {
        this.dateYm = dateYm;
        this.routeId = routeId;
        this.stationId = stationId;
        this.arsId = arsId;
    }
}