package transit.transitwatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BUS_STOP_CROWDING")
public class BusStopCrowding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ITIS_CD", length = 5)
    private String itisCd;

    @Column(name = "SEND_UTC_TIME")
    private Timestamp sendUtcTime;

    @Column(name = "Y_LONGITUDE")
    private Double yLongitude;

    @Column(name = "X_LATITUDE")
    private Double xLatitude;

    @Column(name = "LINK_ID", length = 30)
    private String linkId;

    @Column(name = "ARS_ID", length = 5)
    private String arsId;

    @Column(name = "SEND_PACKET_YEAR", length = 4)
    private String sendPacketYear;

    @Column(name = "SEND_PACKET_MONTH", length = 2)
    private String sendPacketMonth;

    @Column(name = "SEND_PACKET_DAY", length = 2)
    private String sendPacketDay;

    @Column(name = "SEND_PACKET_TIME", length = 6)
    private String sendPacketTime;

    @Column(name = "SEND_PACKET_MILISECOND", length = 4)
    private String sendPacketMilisecond;

    @Column(name = "RECORD_DATE")
    private Timestamp recordDate;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;
}