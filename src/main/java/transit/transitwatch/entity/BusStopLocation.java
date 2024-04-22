package transit.transitwatch.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "BUS_STOP_LOCATION")
public class BusStopLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATION_ID", nullable = false, length = 9, unique = true)
    private String stationId;

    @Column(name = "STATION_NAME", length = 255)
    private String stationName;

    @Column(name = "ARS_ID", nullable = false, length = 5)
    private String arsId;

    @Column(name = "Y_LATITUDE")
    private double yLatitude;

    @Column(name = "X_LONGITUDE")
    private double xLongitude;

    @Column(name = "BUS_STOP_TYPE", length = 30)
    private String busStopType;
}