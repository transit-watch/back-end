package transit.transitwatch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "BUS_STOP_INFO", indexes = {
        @Index(name = "idx_ars_id", columnList = "ARS_ID")
})
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

    @JsonIgnore
    @Column(name = "LINK_ID", length = 10)
    private String linkId;

    @JsonProperty("ylatitude")
    @Column(name = "Y_LATITUDE")
    private double yLatitude;

    @JsonProperty("xlongitude")
    @Column(name = "X_LONGITUDE")
    private double xLongitude;

    @JsonIgnore
    @Column(name = "USE_YN")
    private char useYN; // 사용 여부 (1: 사용, 0: 미사용)

    @JsonIgnore
    @Column(name = "VIRTUAL_BUS_STOP_YN")
    private char virtualBusStopYN; // 가상 정류장 여부 (1: 가상정류장, 0: 일반정류장)

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public BusStopInfo(String stationId, String stationName, String arsId, String linkId, double yLatitude, double xLongitude, char useYN, char virtualBusStopYN) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.arsId = arsId;
        this.linkId = linkId;
        this.yLatitude = yLatitude;
        this.xLongitude = xLongitude;
        this.useYN = useYN;
        this.virtualBusStopYN = virtualBusStopYN;
    }
}