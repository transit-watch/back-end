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
@Table(name = "BUS_STOP_HOURLY_DATA")
public class BusStopHourlyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATISTICS_ID")
    private int statisticsId;

    @Column(name = "HOUR")
    private int hour;

    @Column(name = "UP_PEOPLE")
    private int upPeople;

    @Column(name = "DOWN_PEOPLE")
    private int downPeople;

    @CreationTimestamp
    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Timestamp registerDate;

    @UpdateTimestamp
    @Column(name = "EDIT_DATE")
    private Timestamp editDate;

    public BusStopHourlyData(int statisticsId, int hour, int upPeople, int downPeople) {
        this.statisticsId = statisticsId;
        this.hour = hour;
        this.upPeople = upPeople;
        this.downPeople = downPeople;
    }
}
