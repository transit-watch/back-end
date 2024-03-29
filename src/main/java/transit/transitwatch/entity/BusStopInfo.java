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
@Table(name = "BUS_STOP_INFO")
public class BusStopInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATION_ID", length = 9, unique = true)
    private String stationId;

    @Column(name = "STATION_NAME", length = 255)
    private String stationName;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @Column(name = "LINK_ID", length = 10)
    private String linkId;

    @Column(name = "X_LATITUDE")
    private double xLatitude;

    @Column(name = "Y_LONGITUDE")
    private double yLongitude;

    @Column(name = "USE_YN")
    private char useYN; // 사용 여부 (1: 사용, 0: 미사용)

    @Column(name = "VIRTUAL_BUS_STOP_YN")
    private char virtualBusStopYN; // 가상 정류장 여부 (1: 가상정류장, 0: 일반정류장)

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public BusStopInfo(String stationId, String stationName, String arsId, String linkId, double xLatitude, double yLongitude, char useYN, char virtualBusStopYN) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.linkId = linkId;
        this.xLatitude = xLatitude;
        this.yLongitude = yLongitude;
        this.useYN = useYN;
        this.virtualBusStopYN = virtualBusStopYN;
    }
}