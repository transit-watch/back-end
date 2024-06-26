package transit.transitwatch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @Column(name = "X_LONGITUDE")
    private double xLongitude;

    @Column(name = "Y_LATITUDE")
    private double yLatitude;

    @Column(name = "LINK_ID", length = 30)
    private String linkId;

    @Column(name = "ARS_ID", length = 5, unique = true)
    private String arsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARS_ID", insertable = false, updatable = false)
    private BusStopInfo busStopInfo;

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

    public BusStopCrowding(Long id, String itisCd, Timestamp sendUtcTime, double xLongitude, double yLatitude, String linkId, String arsId, String sendPacketYear, String sendPacketMonth, String sendPacketDay, String sendPacketTime, String sendPacketMilisecond, Timestamp recordDate, Timestamp registerDate, Timestamp editDate) {
        this.id = id;
        this.itisCd = itisCd;
        this.sendUtcTime = sendUtcTime;
        this.xLongitude = xLongitude;
        this.yLatitude = yLatitude;
        this.linkId = linkId;
        this.arsId = arsId;
        this.sendPacketYear = sendPacketYear;
        this.sendPacketMonth = sendPacketMonth;
        this.sendPacketDay = sendPacketDay;
        this.sendPacketTime = sendPacketTime;
        this.sendPacketMilisecond = sendPacketMilisecond;
        this.recordDate = recordDate;
        this.registerDate = registerDate;
        this.editDate = editDate;
    }

}