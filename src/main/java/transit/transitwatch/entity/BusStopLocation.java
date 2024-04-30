package transit.transitwatch.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

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

    @JsonProperty("ylatitude")
    @Column(name = "Y_LATITUDE")
    private double yLatitude;

    @JsonProperty("xlongitude")
    @Column(name = "X_LONGITUDE")
    private double xLongitude;

    @Column(name = "BUS_STOP_TYPE", length = 30)
    private String busStopType;

    @JsonIgnore
    @Column(name = "REGISTER_DATE")
    private Timestamp registerDate;

    @JsonIgnore
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;
}